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

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cpu")
public class CpuUtilController {
	private final CpuUtilService cpuUtilService;

	@GetMapping("/minute")
	public DataResponse<List<SearchMinuteResponse>> searchCpuUtilByMin(

		@NotNull(message = "시작 날짜를 지정해야 합니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH")
		@RequestParam(name = "startDate")
		LocalDateTime startDate,


		@NotNull(message = "끝 날짜를 지정해야 합니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd HH")
		@RequestParam(name = "endDate")
		LocalDateTime endDate
	) {
		return DataResponse.responseBodyData(HttpStatus.OK, cpuUtilService.searchCpuUtilByMin(startDate, endDate));
	}

	@GetMapping("/hour")
	public DataResponse<List<SearchHourResponse>> searchCpuUtilByHour(
		@NotNull(message = "날짜를 지정해야 합니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@RequestParam(name = "pickedDay")
		LocalDate pickedDay
	) {
		return DataResponse.responseBodyData(HttpStatus.OK,cpuUtilService.searchCpuUtilByHour(pickedDay));
	}

	@GetMapping("/day")
	public DataResponse<List<SearchDateResponse>> searchCpuUtilByDays(

		@NotNull(message = "시작 날짜를 지정해야 합니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@RequestParam(name = "startDate")
		LocalDate startDate,

		@NotNull(message = "끝 날짜를 지정해야 합니다.")
		@DateTimeFormat(pattern = "yyyy-MM-dd")
		@RequestParam(name = "endDate")
		LocalDate endDate
	) {
		return DataResponse.responseBodyData(HttpStatus.OK,cpuUtilService.searchCpuUilByDay(startDate, endDate));
	}
}
