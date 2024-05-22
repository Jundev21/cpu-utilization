package com.international.cpuutilization.domain.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.domain.service.CpuUtilService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cpu")
public class CpuUtilController {
	private final CpuUtilService cpuUtilService;

	@GetMapping("/min")
	public ResponseEntity<List<CpuUtilizationEntity>> searchCpuUtilByMin(
		@RequestParam LocalDateTime startDate,
		@RequestParam LocalDateTime endDate
	) {
	return ResponseEntity.ok(cpuUtilService.searchCpuUtilByMin(startDate,endDate));
	}

	@GetMapping("/hour")
	public ResponseEntity<List<CpuUtilizationEntity>> searchCpuUtilByHour(
		@RequestParam LocalDateTime pickedDay
	) {
		return ResponseEntity.ok(cpuUtilService.searchCpuUtilByDay(pickedDay));
	}

	@GetMapping("/day")
	public ResponseEntity<List<CpuUtilizationEntity>> searchCpuUtilByDays(
		@RequestParam LocalDateTime startDate,
		@RequestParam LocalDateTime endDate
	) {
		return ResponseEntity.ok(cpuUtilService.searchCpuByDays(startDate,endDate));
	}
}
