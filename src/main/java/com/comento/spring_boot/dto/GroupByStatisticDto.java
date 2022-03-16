package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class StatisticDto {
	private int requestID;
	private String requestCode;
	private String userID;
	private String createDate;
}
