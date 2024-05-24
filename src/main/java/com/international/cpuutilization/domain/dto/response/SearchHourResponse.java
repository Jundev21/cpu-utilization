package com.international.cpuutilization.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchHourResponse(
	String dateTime,
	int currentHour,
	double minimumUtilization,
	double maximumUtilization,
	double averageUtilization
) {
}
