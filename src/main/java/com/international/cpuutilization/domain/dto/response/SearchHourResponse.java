package com.international.cpuutilization.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchHourResponse(
	int year,
	int month,
	int day,
	int hour,
	int countHours,
	double minimumUtilization,
	double maximumUtilization,
	double averageUtilization
) {
}
