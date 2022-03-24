package com.comento.spring_boot.service;

import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.comento.spring_boot.dto.AvgStatisticDto;
import com.comento.spring_boot.dto.OrganStatisticDto;
import com.comento.spring_boot.dto.StatisticDto;
import com.comento.spring_boot.dto.StatisticObj;
import com.comento.spring_boot.dto.TestDto;
import com.comento.spring_boot.mapper.StatisticMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class StatisticServiceImpl implements StatisticService{
	
	private StatisticMapper mapper;
	private ObjectMapper objMapper;
	
	// ----------test----------
	@Override
	public HashMap<String, Object> yearLoginNum(String year) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		
		try {
			TestDto dto = mapper.selectYearLogin(year);
			dto.setDate("20"+dto.getDate());
			result.put("is_success", true);

			String dataString = objMapper.writeValueAsString(dto);
			
			LinkedHashMap<String, Object> dataMap = objMapper.readValue(dataString, LinkedHashMap.class);
			
			result.put("statistic", dataMap);
		} catch (Exception e) {
			result.put("totCnt", -999);
			result.put("year", year);
			result.put("is_success", false);
		}

		return result;
	}
	
	
	// ----------SW 활용 현황 API----------
	@Override
	public JSONObject monthConnNum() {
		List<StatisticDto> list = mapper.selectGroupByMonth();
		return setResultJson(list);
	}

	@Override
	public JSONObject dayConnNum() {
		List<StatisticDto> list = mapper.selectGroupByDay();
		return setResultJson(list);
	}

	@Override
	public JSONObject organConnNum() {
		List<OrganStatisticDto> list = mapper.selectGroupByOrgan();
		return setResultJson(list);
	}

	@Override
	public JSONObject avgLoginNum() {
		List<AvgStatisticDto> list = mapper.selectAvgGroupByMonth();
		return setResultJson(list);
	}

	@Override
	public JSONObject weekdayLoginNum() throws Exception {
		List<StatisticDto> ymd = mapper.selectLoginGroupByDay();
		List<StatisticDto> ym = mapper.selectLoginGroupByMonth();
		HashMap<String, Integer> removedLogin = excludeHoliday(ymd, ym);
		return setResultList(removedLogin, ym);
	}
	
	public JSONObject setResultJson(List<? extends StatisticObj> list) {
		
		JSONObject json = new JSONObject();
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("is_success", true);
		map.put("data", list);
		json.putAll(map);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonPrinting = gson.toJson(json);
		
		System.out.println(jsonPrinting);
		
		return json;
	}
	
	public JSONObject setResultList(
			HashMap<String, Integer> removedLogin, List<StatisticDto> list) {
		
		for (Entry<String, Integer> entrySet : removedLogin.entrySet()) {
			String yearMonth = entrySet.getKey().substring(0, 6);
			
			for (int j = 0; j < list.size(); j++) {
				StatisticDto dto = list.get(j);
				
				if (yearMonth.equals(dto.getDate())) {
					int totCnt = dto.getTotCnt();
					int rmvLoginCnt = entrySet.getValue();
					dto.setTotCnt(totCnt-rmvLoginCnt);
					list.set(j, dto);
				}
			}
		}
		
		return setResultJson(list);
	}


	public  ArrayList<String> getDateList(NodeList nodeList) {
		ArrayList<String> locdates = new ArrayList<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String tagName = nodeList.item(i).getNodeName();
			if (!"#text".equals(tagName)) {
	
				if (nodeList.item(i).getChildNodes().getLength() > 1) {
					locdates.addAll(getDateList(nodeList.item(i).getChildNodes()));
				} else {
					if (tagName.equals("locdate"))
						locdates.add(nodeList.item(i).getTextContent());
				}
			}
		}
		return locdates;
	}


	public HashMap<String, Integer> excludeHoliday(List<StatisticDto> ymd, List<StatisticDto> ym) throws Exception{
		setCerts();
		// loop로 api 데이터랑 비교해서 맞으면 제외
	
		// 제거된 로그인 수 저장
		HashMap<String, Integer> removedLogin = new HashMap<String, Integer>();
		ArrayList<String> locdates = new ArrayList<String>();
		
		// list마다 date에서 연, 월 데이터 뽑아와서 url에 포함
		for (StatisticDto dto : ym) {
			String date = dto.getDate();
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
	        
	        String urlstr = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?"
					+ "solYear=" + year
					+ "&solMonth=" + month
					+ "&ServiceKey=%2FJn9UW3uqSwZ9bbeKtlFhdcTTcjNM0rhHO0KLb5FYNWKJvksJZFC%2FQFOaTsmloggc2mY0FE0OXaJFWzV%2BRZFOw%3D%3D";
			URL url = new URL(null, urlstr, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = (Document) documentBuilder.parse(new InputSource(urlConnection.getInputStream()));
			document.getDocumentElement().normalize();
			
			
			Node itemRoot = null;
			if ((itemRoot = document.getElementsByTagName("item").item(0))!=null) {
				do {
					if(!itemRoot.hasChildNodes())
						continue;
					NodeList childNodeList = itemRoot.getChildNodes();
					locdates.addAll(getDateList(childNodeList));
					itemRoot = itemRoot.getNextSibling();
				} while (itemRoot != null);
			}
			
			
			urlConnection.disconnect();
		}
	
		// 공휴일과 비교해서 일자가 같으면 제거
		for (int i = 0; i < ymd.size(); i++) {
			
			for (int j = 0; j < locdates.size(); j++) {
				String listDate = ymd.get(i).getDate();
				String locdate = locdates.get(j);
				
				if (listDate.equals(locdate)) {
					String yearMonth = listDate.substring(0, 6);
					int rmvCnt;
					if (removedLogin.get(yearMonth) == null)
						rmvCnt = 1;
					else
						rmvCnt = removedLogin.get(yearMonth)+1;
					removedLogin.put(yearMonth, rmvCnt);
				}
				
			}
			
		}
		
		
		return removedLogin;
	}


	protected void setCerts() {
		TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
			}	
		};
	
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
