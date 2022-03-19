package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class StatisticDto implements StatisticObj{
	private int totCnt;
	private String date;
}
