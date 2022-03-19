package com.comento.spring_boot.service;

import java.util.HashMap;

public interface StatisticService {
	public HashMap<String, Object> yearLoginNum (String year);
	public HashMap<String, Object> monthConnNum();
	public HashMap<String, Object> dayConnNum();
	public HashMap<String, Object> organConnNum();
	public HashMap<String, Object> avgLoginNum();
	public HashMap<String, Object> weekdayLoginNum();
}
