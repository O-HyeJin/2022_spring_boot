package com.comento.spring_boot;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.comento.spring_boot.service.StatisticService;

@RestController
@ResponseBody
@RequestMapping("/login")
public class LogStatisticController {
	
	@Autowired
	private StatisticService service;
	
	@RequestMapping("/groupByMonth/{type}")
	public Map<String, Object> byMonthstt(@PathVariable String type) {
		if (type.equals("average"))
			return service.avgLoginNum();
		else if (type.equals("weekday"))
			return service.weekdayLoginNum();
		else
			return null;
	}
}
