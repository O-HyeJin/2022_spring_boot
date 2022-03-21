package com.comento.spring_boot;


import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comento.spring_boot.service.StatisticService;

@RestController
@ResponseBody
@RequestMapping("/connector")
public class ConnStatisticController {
	
	@Autowired
	private StatisticService service;
	
	@RequestMapping("/groupByMonth")
	public JSONObject byMonthStt() {
		return service.monthConnNum();
	}
	
	@RequestMapping("/groupByDay")
	public JSONObject byDayStt() {
		return service.dayConnNum();
	}
	
	@RequestMapping("/groupByOrgan")
	public JSONObject byOrganStt() {
		return service.organConnNum();
	}
	
	
}
