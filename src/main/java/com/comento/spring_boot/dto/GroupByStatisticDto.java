package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class GroupByStatisticDto {
	private int totCnt;
	private String date;
	private String requestCode;
}
