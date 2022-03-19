package com.comento.spring_boot;

import java.util.Map;

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
	public Map<String, Object> byMonthStt() {
		return service.monthConnNum();
	}
	
	@RequestMapping("/groupByDay")
	public Map<String, Object> byDayStt() {
		return service.dayConnNum();
	}
	
	@RequestMapping("/groupByOrgan")
	public Map<String, Object> byOrganStt() {
		return service.organConnNum();
	}
	
	
}
