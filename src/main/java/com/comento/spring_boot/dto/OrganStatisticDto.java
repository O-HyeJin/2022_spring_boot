package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class OrganStatisticDto implements StatisticObj{
	private int totCnt;
	private String date;
	private String organ;
}
