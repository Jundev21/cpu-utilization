package com.international.cpuutilization.util;

import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Component;

import com.sun.management.OperatingSystemMXBean;

@Component
public class CpuInfo {
	private final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	//	시스템의 CPU 상태 확인
	// 0 일 경우는 사요량이 0 이고 100 일경우는 1로 나타낸다. 따라서
	// cpu 사용량은 퍼센테이지로 나타내기 때문에 *100 을 해서 퍼센테이지로 나타낸다
	public double getCpuUsage() {
		return Math.floor(this.operatingSystemMXBean.getCpuLoad()*1000)/1000;
	}
	public double getJvmUsage() {
		return Math.floor(this.operatingSystemMXBean.getProcessCpuLoad()*1000)/1000;
	}
	public double getFreeMemorySpace() {
		return (double)this.operatingSystemMXBean.getFreeMemorySize();
	}
	public double getTotalMemorySize() {
		return (double)this.operatingSystemMXBean.getTotalMemorySize();
	}
}
