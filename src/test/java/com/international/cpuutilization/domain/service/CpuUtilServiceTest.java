package com.international.cpuutilization.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.international.cpuutilization.domain.dto.QueryDto.SearchDayQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchHourQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchMinuteQueryDto;
import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.repository.CpuUtilizationRepository;

@SpringBootTest
class CpuUtilServiceTest {

	@Autowired
	private CpuUtilService cpuUtilService;
	@MockBean
	private CpuUtilizationRepository cpuUtilizationRepository;

	final int currentYear = LocalDate.now().getYear();
	final int currentMonth = LocalDate.now().getMonthValue();
	final int currentDay = LocalDate.now().getDayOfMonth();

	@Nested
	@DisplayName("cpu 분 조회 서비스 테스트")
	class searchMinTest {

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 1주 1분단위 데이터 조회가 가능하다.")
		public void successMinuteDate() throws Exception {

			//given
			LocalDateTime startDate = LocalDateTime.of(
				currentYear,
				currentMonth,
				currentDay,
				0,
				0
			);

			LocalDateTime endDate = LocalDateTime.of(
				currentYear,
				currentMonth,
				currentDay + 1,
				2,
				0
			);

			List<SearchMinuteQueryDto> tempResult = List.of(
				SearchMinuteQueryDto.builder()
					.year(currentYear)
					.month(currentMonth)
					.day(currentDay)
					.hour(1)
					.minute(1)
					.cpuUtilization(20.00)
					.build()
			);

			given(cpuUtilizationRepository.searchMinData(startDate, endDate)).willReturn(tempResult);

			//when
			List<SearchMinuteResponse> result = cpuUtilService.searchCpuUtilByMin(startDate, endDate);

			//then
			//2024 년 5월 23일 0시부터 2시까지의 1분단위로 조회한 데이터를 반환해야한다.
			// 1시간당 60분이있음으로 0시 부터 2 시까지의 조회 데이터는 총 180개가 있어야한다.
			// 데이터 조회는 2024년 5월 23일 1시 1분에 cpu 사용률 20.00 이 등록되어있다.
			// 총 180개의 데이터중에 2024년 5월 23일 1시 1분 에 등록되어있는 데이터는 61번째 데이터에 존재한다.
			// 따라서 61번쨰 데이터에 해당 데이터가 등록되어있는지 확인

			assertThat(result).isNotNull();
			assertThat(result).hasSize(180);
			assertThat(result.get(61).cpuUtilization()).isEqualTo(tempResult.get(0).getCpuUtilization());

		}

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 1주가 아니면 데이터 조회가 불가능하다.")
		public void failedFilterDate() throws Exception {

			//given
			LocalDateTime pastStartDate = LocalDateTime.now().minusDays(8);
			LocalDateTime endDate = LocalDateTime.of(2024, 5, 23, 2, 0);

			//when,then
			assertThrows(RuntimeException.class, () -> cpuUtilService.searchCpuUtilByMin(pastStartDate, endDate));
		}

	}

	@Nested
	@DisplayName("cpu 시 조회 서비스 테스트")
	class searchHourTest {

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 3달 시간기준 데이터 조회가 가능하다.")
		public void successHourDate() throws Exception {

			//given
			LocalDate pickedDate = LocalDate.of(
				currentYear,
				currentMonth,
				currentDay
			);

			List<SearchHourQueryDto> tempResult = List.of(
				SearchHourQueryDto.builder()
					.year(currentYear)
					.month(currentMonth)
					.day(currentDay)
					.hour(6)
					.min(0.0)
					.max(5.0)
					.avg(3.0)
					.build()
			);

			given(cpuUtilizationRepository.searchHourData(pickedDate)).willReturn(tempResult);

			//when
			List<SearchHourResponse> result = cpuUtilService.searchCpuUtilByHour(pickedDate);

			//then
			// 지정한 날짜의 시 단위로 데이터 조회가 가능해야한다.
			// 2024 년 5월 25일 기준으로 00시 부터 23시까지 총 24개의 데이터를 조회하여 반환해야한다.
			// 데이터 조회는 2024년 5월 25일 6시에 min 0.0, max 5.0, avg 3.0 이 등록되어있다.
			// 2024년 5월 25일 기준으로 총 24개의 데이터중에 6시인 6번째의 데이터를 확인하면된다.

			assertThat(result).isNotNull();
			assertThat(result).hasSize(24);
			assertThat(result.get(6).minimumUtilization()).isEqualTo(tempResult.get(0).getMin());
			assertThat(result.get(6).maximumUtilization()).isEqualTo(tempResult.get(0).getMax());
			assertThat(result.get(6).averageUtilization()).isEqualTo(tempResult.get(0).getAvg());

		}

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 3달이 아니면 데이터 조회가 불가능하다.")
		public void failedFilterDate() throws Exception {

			//given
			LocalDate pickedDate = LocalDate.now().minusMonths(4);
			LocalDate pickedDateMenu = LocalDate.of(2024, 2, 1);

			//when,then
			assertThrows(RuntimeException.class, () -> cpuUtilService.searchCpuUtilByHour(pickedDateMenu));
		}

	}

	@Nested
	@DisplayName("cpu 날짜 조회 서비스 테스트")
	class searchDayTest {

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 1년 날짜기준 데이터 조회가 가능하다.")
		public void successDayDate() throws Exception {

			//given
			LocalDate startDate = LocalDate.of(
				currentYear,
				currentMonth,
				LocalDate.now().getDayOfMonth() - 6
			);

			LocalDate endDate = LocalDate.of(
				currentYear,
				currentMonth,
				LocalDate.now().getDayOfMonth() + 1
			);

			List<SearchDayQueryDto> tempResult = List.of(
				SearchDayQueryDto.builder()
					.year(currentYear)
					.month(currentMonth)
					.day(currentDay)
					.min(0.0)
					.max(5.0)
					.avg(3.0)
					.build()
			);

			given(cpuUtilizationRepository.searchDateData(startDate, endDate)).willReturn(tempResult);

			//when
			List<SearchDateResponse> result = cpuUtilService.searchCpuUilByDay(startDate, endDate);

			//then
			// 2024 년 5월 26일 기준으로 2024 년 5월 20일 부터 2024 년 5월 27일 까지 데이터를 조회하려한다.
			// 날짜 조회임으로 20일부터 27일까지 포함하여 총 8개의 데이터가 조회되어야한다.
			// 임시 더미데이터는 현재기준 26일날의 데이터에 min 값 0.0 max 값 5.0 avg 3.0 이 들어있다.
			// 20일부터 27일까지 총 8 개의 데이터중에 26일은 6번째 데이터에 있다. (0 번부터시작)
			// 따라서 7번째 데이터에 등록되어있는지 확인

			assertThat(result).isNotNull();
			assertThat(result).hasSize(8);
			assertThat(result.get(6).minimumUtilization()).isEqualTo(tempResult.get(0).getMin());
			assertThat(result.get(6).maximumUtilization()).isEqualTo(tempResult.get(0).getMax());
			assertThat(result.get(6).averageUtilization()).isEqualTo(tempResult.get(0).getAvg());
		}

		@Test
		@DisplayName("시작 날짜가 현재 시간 기준으로 최근 1년이 아니면 데이터 조회가 불가능하다.")
		public void failedFilterDate() throws Exception {

			//given
			LocalDate startDate = LocalDate.now().minusMonths(12);
			LocalDate endDate = LocalDate.of(2024, 5, 23);

			//when,then
			assertThrows(RuntimeException.class, () -> cpuUtilService.searchCpuUilByDay(startDate, endDate));
		}

	}

}