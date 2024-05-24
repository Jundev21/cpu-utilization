package com.international.cpuutilization.domain.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.domain.service.CpuUtilService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cpu")
public class CpuUtilController {
	private final CpuUtilService cpuUtilService;

	@GetMapping("/min")
	public ResponseEntity<List<SearchMinuteResponse>> searchCpuUtilByMin(
		@RequestParam     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime startDate,
		@RequestParam     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime endDate
	) {
	return ResponseEntity.ok(cpuUtilService.searchCpuUtilByMin(startDate,endDate));
	}

	@GetMapping("/hour")
	public ResponseEntity<List<SearchHourResponse>> searchCpuUtilByHour(
		@RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd")
		LocalDate pickedDay
	) {
		return ResponseEntity.ok(cpuUtilService.searchCpuUtilByDay(pickedDay));
	}

	@GetMapping("/day")
	public ResponseEntity<List<SearchDateResponse>> searchCpuUtilByDays(
		@RequestParam     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime startDate,
		@RequestParam     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		LocalDateTime endDate
	) {
		return ResponseEntity.ok(cpuUtilService.searchCpuByDays(startDate,endDate));
	}
}
