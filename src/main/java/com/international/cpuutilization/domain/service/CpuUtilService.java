package com.international.cpuutilization.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.domain.repository.CpuUtilizationRepository;
import com.international.cpuutilization.util.CpuInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CpuUtilService {
	private final CpuUtilizationRepository cpuUtilizationRepository;
	private final CpuInfo cpuInfo;
	public List<CpuUtilizationEntity> searchCpuUtilByMin(
		LocalDateTime startDate, LocalDateTime endDate
	) {
		return cpuUtilizationRepository.findAllByCreatedDateBetween(startDate,endDate);
	}

	public List<CpuUtilizationEntity> searchCpuUtilByDay(LocalDateTime pickedDay) {

		return null;
	}

	public List<CpuUtilizationEntity> searchCpuByDays(LocalDateTime startDate, LocalDateTime endDate) {
		return null;
	}

	@Transactional
	@Scheduled(fixedDelay = 60000)
	protected void saveCpuInfo(){
		CpuUtilizationEntity newCpuInfo = new CpuUtilizationEntity(cpuInfo.getCpuUsage(), LocalDateTime.now());
		cpuUtilizationRepository.save(newCpuInfo);
	}
}
