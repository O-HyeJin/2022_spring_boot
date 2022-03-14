package com.comento.spring_boot.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comento.spring_boot.dao.StatisticMapper;

@Service
public class StatisticServiceImpl implements StatisticService{
	
	@Autowired
	private StatisticMapper userMapper;
	
	@Override
	public HashMap<String, Object> yearLoginNum(String year) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			result = userMapper.selectYearLogin(year);
			result.put("year", year);
			result.put("is_success", true);
		} catch (Exception e) {
			result.put("totCnt", -999);
			result.put("year", year);
			result.put("is_success", false);
		}
		return result;
	}

}
