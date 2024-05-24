package com.international.cpuutilization.domain.dto.QueryDto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDayQueryDto {
	private int year;
	private int month;
	private int day;
	private double min;
	private double max;
	private double avg;
}
