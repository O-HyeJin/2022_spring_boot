package com.comento.spring_boot.test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comento.spring_boot.service.StatisticService;
 
@Controller
public class Test {
	
	@Autowired
	private StatisticService service;
	public static int INDENT_FACTOR = 4;
	
	@ResponseBody
	@RequestMapping("/sqlYearStatistic")
	public Map<String, Object> sqlTest(String year) throws Exception {
		return service.yearLoginNum(year);
	}
	
	@RequestMapping("/test") 
    public ModelAndView test() throws Exception{ 
        ModelAndView mav = new ModelAndView("test"); 
        mav.addObject("name", "ohj"); 
        List<String> resultList = new ArrayList<String>(); 
        resultList.add("!!!HELLO WORLD!!!"); 
        resultList.add("설정 TEST!!!"); 
        resultList.add("설정 TEST!!!"); 
        resultList.add("설정 TEST!!!!!"); 
        resultList.add("설정 TEST!!!!!!"); 
        mav.addObject("list", resultList); 
        return mav; 
    }
	
	@ResponseBody
	@RequestMapping("/apiTest")
	public String apiTest() throws Exception {
		String urlstr = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?"
				+ "solYear=2019"
				+ "&solMonth=03"
				+ "&ServiceKey=PHhqZKbAqKUUjsg25R1UiCGgTBFZtkJ7TNML%2BgBjZXf6i0KcscSuLZEB%2Fku3Zp%2FYJ2CS2vkLoOY%2BoE3R%2B3OESw%3D%3D";
		URL url = new URL(urlstr);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
		StringBuffer st = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            st.append(line);
        }
        
        JSONObject xmlJSONObj = XML.toJSONObject(st.toString());
        
        //String jsonPrettyPrintString = xmlJSONObj.toString(INDENT_FACTOR);
	
		JSONObject response = (JSONObject) xmlJSONObj.get("response");
		JSONObject body = (JSONObject) response.get("body");
		JSONObject items = (JSONObject) body.get("items");
		
		//System.out.println(jsonPrettyPrintString);
		return items.toString(INDENT_FACTOR);
		
	}
}
