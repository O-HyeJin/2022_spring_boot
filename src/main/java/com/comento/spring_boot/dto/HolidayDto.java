package com.comento.spring_boot.dto;

import lombok.Data;

@Data
public class HolidayDto {
	private String dateKind;
	private String dateName;
	private String isHoliday;
	private String locdate;
	private int seq;
}
