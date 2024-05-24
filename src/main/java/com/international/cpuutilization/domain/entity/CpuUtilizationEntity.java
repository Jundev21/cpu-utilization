package com.international.cpuutilization.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cpu_utilization")
public class CpuUtilizationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cpuId;
	private double cpuUtilization;
	@CreatedDate
	private LocalDateTime createdDate;

	public CpuUtilizationEntity(double cpuUtilization, LocalDateTime createdDate) {
		this.cpuUtilization = cpuUtilization;
		this.createdDate = createdDate;
	}
}
