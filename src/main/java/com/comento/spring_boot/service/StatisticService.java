package com.comento.spring_boot.service;

import java.util.HashMap;

import org.json.simple.JSONObject;

public interface StatisticService {
	public HashMap<String, Object> yearLoginNum (String year);
	public JSONObject monthConnNum();
	public JSONObject dayConnNum();
	public JSONObject organConnNum();
	public JSONObject avgLoginNum();
	public JSONObject weekdayLoginNum() throws Exception;
}
