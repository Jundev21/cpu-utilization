package com.international.cpuutilization.domain.dto.response;

import lombok.Builder;

@Builder
public record SearchDateResponse(
	String dateTime,
	int currentDay,
	double minimumUtilization,
	double maximumUtilization,
	double averageUtilization
) {
}
