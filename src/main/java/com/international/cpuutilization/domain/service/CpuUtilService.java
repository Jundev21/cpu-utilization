package com.international.cpuutilization.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.domain.entity.QCpuUtilizationEntity;
import com.international.cpuutilization.domain.repository.CpuUtilizationRepository;
import com.international.cpuutilization.util.CpuInfo;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CpuUtilService {
	private final CpuUtilizationRepository cpuUtilizationRepository;
	private final CpuInfo cpuInfo;
	public List<SearchMinuteResponse> searchCpuUtilByMin(
		LocalDateTime startDate, LocalDateTime endDate
	) {
		return cpuUtilizationRepository.searchMinData(startDate, endDate);
	}

	public List<SearchHourResponse> searchCpuUtilByDay(LocalDate pickedDay) {
		return cpuUtilizationRepository.searchHourData(pickedDay);
	}

	public List<SearchDateResponse> searchCpuByDays(LocalDateTime startDate, LocalDateTime endDate) {
		return cpuUtilizationRepository.searchDateData(startDate,endDate);
	}

	@Transactional
	@Scheduled(fixedDelay = 60000)
	protected void saveCpuInfo(){
		CpuUtilizationEntity newCpuInfo = new CpuUtilizationEntity(cpuInfo.getCpuUsage(), LocalDateTime.now());
		cpuUtilizationRepository.save(newCpuInfo);
	}
}
