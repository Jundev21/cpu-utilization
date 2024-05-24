package com.international.cpuutilization.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchMinuteResponse(
	int year,
	int month,
	int day,
	int hour,
	int minute,
	int countMinutes,
	double cpuUtilization
) {
}
