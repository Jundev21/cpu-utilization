package com.international.cpuutilization.domain.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.service.CpuUtilService;
@SpringBootTest
@AutoConfigureMockMvc
class CpuUtilControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private CpuUtilService cpuUtilService;

	@Nested
	@DisplayName("cpu 분 조회 컨트롤러 테스트")
	class searchMinTest {

		private final String startDate = LocalDateTime.of(2024, 4, 27, 0, 27).toString();
		private final String endDate = LocalDateTime.of(2024, 4, 27, 4, 27).toString();

		private final List<SearchMinuteResponse> getData =
			List.of(
				SearchMinuteResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.hour(2)
					.minute(1)
					.countMinutes(1)
					.cpuUtilization(4.27)
					.build()
				,
				SearchMinuteResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.hour(5)
					.minute(5)
					.countMinutes(5)
					.cpuUtilization(4.35)
					.build()
			);

		@Test
		@DisplayName("시작 날짜와 끝 날짜가 정상적으로 받아와지고 데이터가 성공적으로 반환된다.")
		public void successStartEndDate() throws Exception {

			given(cpuUtilService.searchCpuUtilByMin(any(LocalDateTime.class), any(LocalDateTime.class)))
				.willReturn(getData);

			mockMvc.perform(get("/api/cpu/minute")
					.param("startDate", startDate)
					.param("endDate", endDate))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].cpuUtilization").value(getData.get(0).cpuUtilization()))
				.andExpect(jsonPath("$.data.[1].cpuUtilization").value(getData.get(1).cpuUtilization()));

		}

		@Test
		@DisplayName("시작 날짜 형태(yyyy-MM-dd HH)가 올바르지 않는다.")
		public void failStartDate() throws Exception {
			mockMvc.perform(get("/api/cpu/minute")
					.param("startDate", "2024-04-27")
					.param("endDate", endDate))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("끝 날짜 형태(yyyy-MM-dd HH)가 올바르지 않는다.")
		public void failEndDate() throws Exception {
			mockMvc.perform(get("/api/cpu/minute")
					.param("startDate", startDate)
					.param("endDate", "2024-04-27"))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("시작 날짜를 입력하지 않았다.")
		public void missStartDate() throws Exception {
			mockMvc.perform(get("/api/cpu/minute")
					.param("endDate", endDate))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("끝 날짜를 입력하지 않았다.")
		public void missEndDate() throws Exception {
			mockMvc.perform(get("/api/cpu/minute")
					.param("startDate", startDate))
				.andExpect(status().isBadRequest());
		}

	}

	@Nested
	@DisplayName("cpu 시 조회 컨트롤러테스트")
	class searchHourTest {
		private final String pickedDay = LocalDate.of(2024, 4, 27).toString();
		private final List<SearchHourResponse> getData =
			List.of(
				SearchHourResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.hour(2)
					.countHours(1)
					.minimumUtilization(4.27)
					.maximumUtilization(40.27)
					.averageUtilization(23.5)
					.build()
				,
				SearchHourResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.hour(2)
					.countHours(2)
					.minimumUtilization(5.27)
					.maximumUtilization(55.27)
					.averageUtilization(29.5)
					.build()
			);

		@Test
		@DisplayName("날짜를 선택하고 데이터를 성공적으로 반환한다.")
		public void successPickedDate() throws Exception {

			given(cpuUtilService.searchCpuUtilByHour(any(LocalDate.class)))
				.willReturn(getData);

			mockMvc.perform(get("/api/cpu/hour")
					.param("pickedDay", pickedDay))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].minimumUtilization").value(getData.get(0).minimumUtilization()))
				.andExpect(jsonPath("$.data.[0].maximumUtilization").value(getData.get(0).maximumUtilization()))
				.andExpect(jsonPath("$.data.[0].averageUtilization").value(getData.get(0).averageUtilization()))
				.andExpect(jsonPath("$.data.[1].minimumUtilization").value(getData.get(1).minimumUtilization()))
				.andExpect(jsonPath("$.data.[1].maximumUtilization").value(getData.get(1).maximumUtilization()))
				.andExpect(jsonPath("$.data.[1].averageUtilization").value(getData.get(1).averageUtilization()));

		}

		@Test
		@DisplayName("선택 날짜 형태(yyyy-MM-dd)가 올바르지 않는다.")
		public void failStartDate() throws Exception {
			mockMvc.perform(get("/api/cpu/hour")
					.param("pickedDay", "24-4-27"))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("선택 날짜를 입력하지 않았다.")
		public void failEndDate() throws Exception {
			mockMvc.perform(get("/api/cpu/hour"))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("cpu 날짜 조회 컨트롤러 테스트")
	class searchDayTest {
		private final String startDate = LocalDate.of(2024, 4, 27).toString();
		private final String endDate = LocalDate.of(2024, 4, 27).toString();

		private final List<SearchDateResponse> getData =
			List.of(
				SearchDateResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.day(2)
					.minimumUtilization(5.27)
					.maximumUtilization(55.27)
					.averageUtilization(29.5)
					.build()
				,
				SearchDateResponse.builder()
					.year(2024)
					.month(4)
					.day(27)
					.day(2)
					.minimumUtilization(5.27)
					.maximumUtilization(55.27)
					.averageUtilization(29.5)
					.build()
			);

		@Test
		@DisplayName("시작 날짜와 끝 날짜가 정상적으로 받아와지고 데이터가 성공적으로 반환된다..")
		public void successStartEndDate() throws Exception {

			given(cpuUtilService.searchCpuUilByDay(any(LocalDate.class), any(LocalDate.class)))
				.willReturn(getData);

			mockMvc.perform(get("/api/cpu/day")
					.param("startDate", startDate)
					.param("endDate", endDate))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.[0].minimumUtilization").value(getData.get(0).minimumUtilization()))
				.andExpect(jsonPath("$.data.[0].maximumUtilization").value(getData.get(0).maximumUtilization()))
				.andExpect(jsonPath("$.data.[0].averageUtilization").value(getData.get(0).averageUtilization()))
				.andExpect(jsonPath("$.data.[1].minimumUtilization").value(getData.get(1).minimumUtilization()))
				.andExpect(jsonPath("$.data.[1].maximumUtilization").value(getData.get(1).maximumUtilization()))
				.andExpect(jsonPath("$.data.[1].averageUtilization").value(getData.get(1).averageUtilization()));

		}

		@Test
		@DisplayName("시작 날짜 형태(yyyy-MM-dd HH)가 올바르지 않는다.")
		public void failStartDate() throws Exception {
			mockMvc.perform(get("/api/cpu/day")
					.param("startDate", "24-04-27")
					.param("endDate", endDate))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("끝 날짜 형태(yyyy-MM-dd)가 올바르지 않는다.")
		public void failEndDate() throws Exception {
			mockMvc.perform(get("/api/cpu/day")
					.param("startDate", startDate)
					.param("endDate", "24-04-27"))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("시작 날짜를 입력하지 않았다.")
		public void missStartDate() throws Exception {
			mockMvc.perform(get("/api/cpu/day")
					.param("endDate", endDate))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("끝 날짜를 입력하지 않았다.")
		public void missEndDate() throws Exception {
			mockMvc.perform(get("/api/cpu/day")
					.param("startDate", startDate))
				.andExpect(status().isBadRequest());
		}

	}
}