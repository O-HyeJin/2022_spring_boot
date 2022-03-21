package com.comento.spring_boot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.comento.spring_boot.dto.AvgStatisticDto;
import com.comento.spring_boot.dto.OrganStatisticDto;
import com.comento.spring_boot.dto.StatisticDto;
import com.comento.spring_boot.dto.TestDto;

@Repository
@Mapper
public interface StatisticMapper {
	// 테스트용
	public TestDto selectYearLogin(String year);
	
	// SW 활용 현황 API
	public List<StatisticDto> selectGroupByMonth();
	public List<StatisticDto> selectGroupByDay();
	public List<OrganStatisticDto> selectGroupByOrgan();
	public List<AvgStatisticDto> selectAvgGroupByMonth();
	public List<StatisticDto> selectLoginGroupByMonth();
	public List<StatisticDto> selectLoginGroupByDay();
}
