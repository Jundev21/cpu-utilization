package com.international.cpuutilization.domain.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.international.cpuutilization.domain.dto.QueryDto.SearchDayQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchHourQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchMinuteQueryDto;
import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.domain.repository.CpuUtilizationRepository;
import com.international.cpuutilization.exception.BasicException;
import com.international.cpuutilization.exception.ErrorCode;
import com.international.cpuutilization.util.CpuInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CpuUtilService {
	private final CpuUtilizationRepository cpuUtilizationRepository;
	private final CpuInfo cpuInfo;

	@Transactional(readOnly = true)
	public List<SearchMinuteResponse> searchCpuUtilByMin(
		LocalDateTime startDate, LocalDateTime endDate
	) {
		isValidDateForMinute(startDate);
		List<SearchMinuteQueryDto> getResults = cpuUtilizationRepository.searchMinData(startDate, endDate);
		return getSearchMinuteResponses(startDate, endDate, getResults);
	}

	@Transactional(readOnly = true)
	public List<SearchHourResponse> searchCpuUtilByHour(LocalDate pickedDay) {
		isValidDateForHour(pickedDay);
		List<SearchHourQueryDto> getResults = cpuUtilizationRepository.searchHourData(pickedDay);
		return getSearchHourResponses(pickedDay, getResults);
	}

	@Transactional(readOnly = true)
	public List<SearchDateResponse> searchCpuUilByDay(LocalDate startDate, LocalDate endDate) {
		isValidDateForDay(startDate);
		List<SearchDayQueryDto> getResults = cpuUtilizationRepository.searchDateData(startDate, endDate);
		return getSearchDateResponses(startDate, endDate, getResults);
	}

	private List<SearchMinuteResponse> getSearchMinuteResponses(LocalDateTime startDate, LocalDateTime endDate,
		List<SearchMinuteQueryDto> getResults) {
		List<SearchMinuteResponse> result = new ArrayList<>();
		int startHour = startDate.getHour();
		int endHour = endDate.getHour();

		for (int i = startHour; i <= endHour; i++) {
			for (int j = 0; j < 60; j++) {
				boolean hasMinuteData = false;
				for (SearchMinuteQueryDto searchMinute : getResults) {
					if (searchMinute.getHour() == i && searchMinute.getMinute() == j) {
						SearchMinuteResponse newData = SearchMinuteResponse.builder()
							.year(searchMinute.getYear())
							.month(searchMinute.getMonth())
							.day(searchMinute.getDay())
							.hour(searchMinute.getHour())
							.minute(searchMinute.getMinute())
							.countMinutes(j)
							.cpuUtilization(searchMinute.getCpuUtilization())
							.build();
						hasMinuteData = true;
						result.add(newData);
					}
				}
				if (!hasMinuteData) {
					SearchMinuteResponse newData = SearchMinuteResponse.builder()
						.year(startDate.getYear())
						.month(startDate.getMonthValue())
						.day(startDate.getDayOfMonth())
						.hour(i)
						.minute(j)
						.countMinutes(j)
						.cpuUtilization(0.0)
						.build();
					result.add(newData);
				}
			}
		}

		return result;
	}

	private void isValidDateForMinute(LocalDateTime startDate) {
		final int eightDaysAgo = 7;
		// 분 단위 API : 최근 1주 데이터 제공
		LocalDate getPastDays = LocalDate.now().minusDays(eightDaysAgo);
		if (!startDate.toLocalDate().isAfter(getPastDays)) {
			throw new BasicException(ErrorCode.LIMIT_MINUTES_ERROR, ErrorCode.LIMIT_MINUTES_ERROR.getErrorMessage());
		}
	}

	private List<SearchHourResponse> getSearchHourResponses(LocalDate pickedDay, List<SearchHourQueryDto> getResults) {
		List<SearchHourResponse> result = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			boolean hasHourData = false;
			for (SearchHourQueryDto searchHour : getResults) {
				if (searchHour.getHour() == i) {
					SearchHourResponse newData = SearchHourResponse.builder()
						.year(searchHour.getYear())
						.month(searchHour.getMonth())
						.day(searchHour.getDay())
						.hour(searchHour.getHour())
						.countHours(i)
						.minimumUtilization(searchHour.getMin())
						.maximumUtilization(searchHour.getMax())
						.averageUtilization(mathFloorMethod(searchHour.getAvg()))
						.build();
					hasHourData = true;
					result.add(newData);
				}
			}
			if (!hasHourData) {
				SearchHourResponse nonData = SearchHourResponse.builder()
					.year(pickedDay.getYear())
					.month(pickedDay.getMonthValue())
					.day(pickedDay.getDayOfMonth())
					.hour(i)
					.countHours(i)
					.minimumUtilization(0.0)
					.maximumUtilization(0.0)
					.averageUtilization(0.0)
					.build();
				result.add(nonData);
			}
		}
		return result;
	}

	private void isValidDateForHour(LocalDate pickedDay) {
		final int fourMonthAgo = 3;
		//시 단위 API : 최근 3달 데이터 제공
		LocalDate getPastMonth = LocalDate.now().minusMonths(fourMonthAgo);

		if (!pickedDay.isAfter(getPastMonth)) {
			throw new BasicException(ErrorCode.LIMIT_HOUR_ERROR, ErrorCode.LIMIT_HOUR_ERROR.getErrorMessage());
		}
	}

	private List<SearchDateResponse> getSearchDateResponses(LocalDate startDate, LocalDate endDate,
		List<SearchDayQueryDto> getResults) {
		List<SearchDateResponse> result = new ArrayList<>();
		while (startDate.compareTo(endDate) < 1) {
			boolean hasDayData = false;
			for (SearchDayQueryDto searchDay : getResults) {
				if (startDate.isEqual(LocalDate.of(searchDay.getYear(), searchDay.getMonth(), searchDay.getDay()))) {
					SearchDateResponse newData = SearchDateResponse.builder()
						.year(searchDay.getYear())
						.month(searchDay.getMonth())
						.day(searchDay.getDay())
						.minimumUtilization(searchDay.getMin())
						.maximumUtilization(searchDay.getMax())
						.averageUtilization(mathFloorMethod(searchDay.getAvg()))
						.build();
					hasDayData = true;
					result.add(newData);
				}
			}

			if (!hasDayData) {
				SearchDateResponse nonData = SearchDateResponse.builder()
					.year(startDate.getYear())
					.month(startDate.getMonthValue())
					.day(startDate.getDayOfMonth())
					.minimumUtilization(0.0)
					.maximumUtilization(0.0)
					.averageUtilization(0.0)
					.build();
				result.add(nonData);
			}
			startDate = startDate.plusDays(1);
		}
		return result;
	}

	private void isValidDateForDay(LocalDate startDate) {
		final int oneYearAgo = 1;
		//일 단위 API : 최근 1년 데이터 제공
		LocalDate getPastYear = LocalDate.now().minusYears(oneYearAgo);

		if (!startDate.isAfter(getPastYear)) {
			throw new BasicException(ErrorCode.LIMIT_DAY_ERROR, ErrorCode.LIMIT_DAY_ERROR.getErrorMessage());
		}
	}

	private double mathFloorMethod(double averageValue) {
		return Math.floor(averageValue * 1000) / 1000;
	}

	@Transactional
	@Scheduled(fixedDelay = 60000)
	protected void saveCpuInfo() {
		CpuUtilizationEntity newCpuInfo = new CpuUtilizationEntity(cpuInfo.getCpuUsage(), LocalDateTime.now());
		cpuUtilizationRepository.save(newCpuInfo);
	}
}
