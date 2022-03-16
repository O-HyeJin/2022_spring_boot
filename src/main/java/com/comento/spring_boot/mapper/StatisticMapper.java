package com.comento.spring_boot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.comento.spring_boot.dto.GroupByStatisticDto;

@Repository
@Mapper
public interface StatisticMapper {
	public GroupByStatisticDto selectYearLogin(String year); 
}
