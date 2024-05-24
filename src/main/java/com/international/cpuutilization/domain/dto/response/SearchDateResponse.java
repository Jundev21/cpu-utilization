package com.international.cpuutilization.domain.dto.response;

import lombok.Builder;

@Builder
public record SearchDateResponse(
	int year,
	int month,
	int day,
	double minimumUtilization,
	double maximumUtilization,
	double averageUtilization
) {
}
