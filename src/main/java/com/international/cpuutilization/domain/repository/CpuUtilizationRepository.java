package com.international.cpuutilization.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;

public interface CpuUtilizationRepository extends JpaRepository<CpuUtilizationEntity,Long>, CpuUtilizationQueryDslRepository {
	List<CpuUtilizationEntity> findAllByCreatedDateBetween(LocalDateTime startTime, LocalDateTime endTime);
}
