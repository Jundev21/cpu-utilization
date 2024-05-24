package com.international.cpuutilization.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.querydsl.core.Tuple;

public interface CpuUtilizationQueryDslRepository {
	List<SearchMinuteResponse> searchMinData(LocalDateTime startDate, LocalDateTime endDate);
	List<SearchHourResponse> searchHourData(LocalDateTime pickedDay);
	List<SearchDateResponse> searchDateData(LocalDateTime startDate, LocalDateTime endDate);
}
