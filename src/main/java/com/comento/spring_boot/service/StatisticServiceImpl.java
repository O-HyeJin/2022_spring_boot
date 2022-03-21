package com.comento.spring_boot.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.comento.spring_boot.dto.AvgStatisticDto;
import com.comento.spring_boot.dto.HolidayDto;
import com.comento.spring_boot.dto.OrganStatisticDto;
import com.comento.spring_boot.dto.StatisticDto;
import com.comento.spring_boot.dto.StatisticObj;
import com.comento.spring_boot.dto.TestDto;
import com.comento.spring_boot.mapper.StatisticMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
		return setResultMap(list);
	}

	@Override
	public JSONObject dayConnNum() {
		List<StatisticDto> list = mapper.selectGroupByDay();
		return setResultMap(list);
	}

	@Override
	public JSONObject organConnNum() {
		List<OrganStatisticDto> list = mapper.selectGroupByOrgan();
		return setResultMap(list);
	}

	@Override
	public JSONObject avgLoginNum() {
		List<AvgStatisticDto> list = mapper.selectAvgGroupByMonth();
		return setResultMap(list);
	}

	@Override
	public JSONObject weekdayLoginNum() throws Exception {
		List<StatisticDto> ymd = mapper.selectLoginGroupByDay();
		List<StatisticDto> ym = mapper.selectLoginGroupByMonth();
		return excludeHoliday(ymd);
	}
	
	public JSONObject setResultMap(List<? extends StatisticObj> list) {
		//LinkedHashMap<String, Object>
		/*
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		try {
			result.put("is_success", true);
			
			for (int i=0;i<list.size();i++) {
				String dataString = objMapper.writeValueAsString(list.get(i));
				LinkedHashMap<String, Object> dataMap = objMapper.readValue(dataString, LinkedHashMap.class);
				result.put("no"+(i+1), dataMap);
			}
		} catch (Exception e) {
			result.put("totCnt", -999);
			result.put("is_success", false);
		}
		*/
		
		JSONObject json = new JSONObject();
		json.put("is_success", true);
		json.put("data", list);

		return json;
	}


	public JSONObject excludeHoliday(List<StatisticDto> list) throws Exception{
		setCerts();
		// loop로 api 데이터랑 비교해서 맞으면 제외

		// 공휴일 리스트 생성하기
		List<HolidayDto> holidays = new ArrayList<HolidayDto>();

		// list마다 date에서 연, 월 데이터 뽑아와서 url에 포함
		for (StatisticDto dto : list) {
			String date = dto.getDate();
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "%2FJn9UW3uqSwZ9bbeKtlFhdcTTcjNM0rhHO0KLb5FYNWKJvksJZFC%2FQFOaTsmloggc2mY0FE0OXaJFWzV%2BRZFOw%3D%3D"); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
	        urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/
	        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*타입*/
	        
	        String urlstr = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?"
					+ "solYear=" + year
					+ "&solMonth=" + month
					+ "&ServiceKey=%2FJn9UW3uqSwZ9bbeKtlFhdcTTcjNM0rhHO0KLb5FYNWKJvksJZFC%2FQFOaTsmloggc2mY0FE0OXaJFWzV%2BRZFOw%3D%3D"
					+ "&type=json";
			URL url = new URL(null, urlstr, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			StringBuffer st = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				st.append(line);
			}

			// 데이터 중 items만 가져옴
			
			JSONParser jsonParser = new JSONParser();
			System.out.println(st.toString());
			JSONObject json = (JSONObject) jsonParser.parse(st.toString());
			JSONObject response = (JSONObject) json.get("response");
			System.out.println(response);
			JSONObject body = (JSONObject) response.get("body");
			JSONArray items = (JSONArray) body.get("items");

			Gson gson = new Gson();

			for (Object arr : items) {
				holidays.add(gson.fromJson(arr.toString(), HolidayDto.class));
			}
			
			urlConnection.disconnect();
		}

		// 공휴일과 비교해서 일자가 같으면 제거
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < holidays.size(); j++) {
				if (list.get(i).getDate().equals(holidays.get(j).getLocdate())) {
					list.remove(i);
				}
			}
		}
		
		
		return setResultMap(list);
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
