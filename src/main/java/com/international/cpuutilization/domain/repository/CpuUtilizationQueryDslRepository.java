package com.international.cpuutilization.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.international.cpuutilization.domain.dto.QueryDto.SearchDayQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchHourQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchMinuteQueryDto;

public interface CpuUtilizationQueryDslRepository {
	List<SearchMinuteQueryDto> searchMinData(LocalDateTime startDate, LocalDateTime endDate);

	List<SearchHourQueryDto> searchHourData(LocalDate pickedDay);

	List<SearchDayQueryDto> searchDateData(LocalDate startDate, LocalDate endDate);
}
