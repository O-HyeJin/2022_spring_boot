package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class AvgStatisticDto implements StatisticObj{
	private double totCnt;
	private String date;
}
