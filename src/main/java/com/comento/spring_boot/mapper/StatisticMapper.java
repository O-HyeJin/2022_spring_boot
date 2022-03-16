package com.comento.spring_boot.dao;

import java.util.HashMap;
import com.comento.spring_boot.dto.StatisticDto;

public interface StatisticMapper {
	public HashMap<String, Object> selectYearLogin(String year); 
}
