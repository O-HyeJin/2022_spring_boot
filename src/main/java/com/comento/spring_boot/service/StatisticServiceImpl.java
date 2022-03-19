package com.comento.spring_boot.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.comento.spring_boot.dto.AvgStatisticDto;
import com.comento.spring_boot.dto.OrganStatisticDto;
import com.comento.spring_boot.dto.StatisticDto;
import com.comento.spring_boot.dto.StatisticObj;
import com.comento.spring_boot.dto.TestDto;
import com.comento.spring_boot.mapper.StatisticMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public HashMap<String, Object> monthConnNum() {
		List<StatisticDto> list = mapper.selectGroupByMonth();
		return setResultMap(list);
	}

	@Override
	public HashMap<String, Object> dayConnNum() {
		List<StatisticDto> list = mapper.selectGroupByDay();
		return setResultMap(list);
	}

	@Override
	public HashMap<String, Object> organConnNum() {
		List<OrganStatisticDto> list = mapper.selectGroupByOrgan();
		return setResultMap(list);
	}

	@Override
	public HashMap<String, Object> avgLoginNum() {
		List<AvgStatisticDto> list = mapper.selectAvgGroupByMonth();
		return setResultMap(list);
	}

	@Override
	public HashMap<String, Object> weekdayLoginNum() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public LinkedHashMap<String, Object> setResultMap(List<? extends StatisticObj> list) {
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
		
		return result;
	}


}
