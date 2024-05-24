package com.international.cpuutilization.domain.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.international.cpuutilization.common.DataResponse;
import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.service.CpuUtilService;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cpu")
public class CpuUtilController {
	private final CpuUtilService cpuUtilService;

	@GetMapping("/min")
	public DataResponse<List<SearchMinuteResponse>> searchCpuUtilByMin(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH")
		LocalDateTime startDate,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH")
		LocalDateTime endDate
	) {
		return DataResponse.responseBodyData(HttpStatus.OK, cpuUtilService.searchCpuUtilByMin(startDate, endDate));
	}

	@GetMapping("/hour")
	public DataResponse<List<SearchHourResponse>> searchCpuUtilByHour(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate pickedDay
	) {
		return DataResponse.responseBodyData(HttpStatus.OK,cpuUtilService.searchCpuUtilByHour(pickedDay));
	}

	@GetMapping("/day")
	public DataResponse<List<SearchDateResponse>> searchCpuUtilByDays(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate startDate,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate endDate
	) {
		return DataResponse.responseBodyData(HttpStatus.OK,cpuUtilService.searchCpuUilByDay(startDate, endDate));
	}
}
