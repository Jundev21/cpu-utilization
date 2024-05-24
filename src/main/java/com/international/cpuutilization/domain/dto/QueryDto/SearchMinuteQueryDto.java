package com.international.cpuutilization.domain.dto.QueryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchMinuteQueryDto{
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private double cpuUtilization;
}
