package com.comento.spring_boot.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comento.spring_boot.dto.GroupByStatisticDto;
import com.comento.spring_boot.mapper.StatisticMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StatisticServiceImpl implements StatisticService{
	
	@Autowired
	private StatisticMapper mapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, Object> yearLoginNum(String year) {
		LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
		
		try {
			GroupByStatisticDto dto = mapper.selectYearLogin(year);
			dto.setDate("20"+dto.getDate());
			result.put("is_success", true);

			ObjectMapper objMapper = new ObjectMapper(); 
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

}
