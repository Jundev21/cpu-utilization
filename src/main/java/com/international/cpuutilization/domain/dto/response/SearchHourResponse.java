package com.international.cpuutilization.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchHourResponse(
	String dateTime,
	int countHour,
	int currentHour,
	double minimumUtilization,
	double maximumUtilization,
	double averageUtilization
) {
}
