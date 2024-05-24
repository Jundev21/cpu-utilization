package com.international.cpuutilization.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchMinuteResponse(
	String dateTime,
	int minutes,
	int currHours,
	int currMinutes,
	double cpuUtilization
) {
}
