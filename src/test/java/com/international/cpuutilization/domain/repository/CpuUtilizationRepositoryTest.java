package com.international.cpuutilization.domain.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.international.cpuutilization.config.QueryDslConfig;
import com.international.cpuutilization.domain.dto.QueryDto.SearchDayQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchHourQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchMinuteQueryDto;
import com.international.cpuutilization.domain.entity.CpuUtilizationEntity;
import com.international.cpuutilization.util.CpuInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@Import(QueryDslConfig.class)
class CpuUtilizationRepositoryTest {
	@Autowired
	private CpuUtilizationRepository cpuUtilizationRepository;

	@MockBean
	private CpuInfo cpuInfo;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@DisplayName("CPU 데이터 저장이 가능하다.")
	public void saveMinutesData() {
		CpuUtilizationEntity newCpuInfo = new CpuUtilizationEntity(cpuInfo.getCpuUsage(), LocalDateTime.now());
		CpuUtilizationEntity result = cpuUtilizationRepository.save(newCpuInfo);

		assertThat(result.getCpuId()).isEqualTo(newCpuInfo.getCpuId());
	}

	@Test
	@DisplayName("minutes 데이터 조회가 가능하다.")
	public void searchMinutesData() {

		List<CpuUtilizationEntity> newCpuInfo = new ArrayList<>();
		LocalDateTime startDate = LocalDateTime.of(2024, 5, 1, 1, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 5, 1, 2, 0);

		// 2024년 5월 1일 1시 1분
		// 분단위 조회는 시간사이 데이터들 조회가 가능하다.
		// 예를 들어 2024년 5월 1일 1시 0분 부터 2024년 5월 1일 2시 0분 데이터 조회를 시작하면
		// 2024년 5월 1일 1시 0분부터 2024년 5월 1일 1시 59분까지 데이터가 있는지 조회한다.

		// 테스트로 5월 1일부터 5일까지 각 5개의 데이터를 i+3개씩 삽입한다.
		// 예를들어 5월 1일 1시 1분, 5월 1일 1시 2분, 5월 1일 1시 3분

		for (int i = 1; i < 5; i++) {
			newCpuInfo.add(new CpuUtilizationEntity(i + 2, LocalDateTime.of(2024, 5, i, i, i)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 3, LocalDateTime.of(2024, 5, i, i, i + 1)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 4, LocalDateTime.of(2024, 5, i, i, i + 2)));
		}

		List<CpuUtilizationEntity> tempResult = cpuUtilizationRepository.saveAll(newCpuInfo);

		// 5월 1일 1시, 2시,3시 3개의 데이터가 삽입되어있다.
		// 5월 1일 1시부터 5월 1일 2시까지 데이터 조회시 3개의 데이터가 나와야한다.
		// 각 cpu 사용률은 3,4,5
		List<SearchMinuteQueryDto> results = cpuUtilizationRepository.searchMinData(startDate, endDate);

		assertThat(results.size()).isEqualTo(3);
		assertThat(results.get(0).getCpuUtilization()).isEqualTo(3);
		assertThat(results.get(1).getCpuUtilization()).isEqualTo(4);
		assertThat(results.get(2).getCpuUtilization()).isEqualTo(5);
	}

	@Test
	@DisplayName("hour 데이터 조회가 가능하다.")
	public void searchHourData() {

		List<CpuUtilizationEntity> newCpuInfo = new ArrayList<>();
		LocalDate pickDate = LocalDate.of(2024, 5, 1);

		// 2024년 5월 1일 데이터 조회시 5월 1일의 시간단위 조회가 가능해야한다
		// 즉 5월 1일 0시부터 5월 1일 23시 까지 모든 데이터가 조회되어야한다.

		// 테스트로 5월 1일에 1시부터 5시까지 각 분마다 3개씩 삽입한다.
		// 예를들어
		// 5월 1일 1시 2분, 5월 1일 1시 3분, 5월 1일 1시 4분
		// 5월 1일 2시 3분, 5월 1일 2시 4분, 5월 1일 2시 5분
		// 5월 1일 3시 4분, 5월 1일 3시 5분, 5월 1일 3시 6분
		// 5월 1일 4시 5분, 5월 1일 4시 6분, 5월 1일 4시 7분

		// 시간단위 조회 임으로 총 4개의 데이터가 나와야한다.

		for (int i = 1; i < 5; i++) {
			newCpuInfo.add(new CpuUtilizationEntity(i + 1, LocalDateTime.of(2024, 5, 1, i, i + 1)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 2, LocalDateTime.of(2024, 5, 1, i, i + 2)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 3, LocalDateTime.of(2024, 5, 1, i, i + 3)));
		}

		List<CpuUtilizationEntity> tempResult = cpuUtilizationRepository.saveAll(newCpuInfo);

		// 5월 1일 1시, 2시, 3시, 4시 4개의 데이터가 삽입되어있다.
		// 총 4개의 데이터가 나와야하고
		// 1시 최소사용률 2, 2시 최소사용율 3, 3시 최소사용율 4, 4시 최소사용율 5가 나와야한다.

		List<SearchHourQueryDto> results = cpuUtilizationRepository.searchHourData(pickDate);

		assertThat(results.size()).isEqualTo(4);
		assertThat(results.get(0).getHour()).isEqualTo(1);
		assertThat(results.get(1).getHour()).isEqualTo(2);
		assertThat(results.get(2).getHour()).isEqualTo(3);
		assertThat(results.get(3).getHour()).isEqualTo(4);

		assertThat(results.get(0).getMin()).isEqualTo(2);
		assertThat(results.get(1).getMin()).isEqualTo(3);
		assertThat(results.get(2).getMin()).isEqualTo(4);
		assertThat(results.get(3).getMin()).isEqualTo(5);
	}

	@Test
	@DisplayName("day 데이터 조회가 가능하다.")
	public void searchDayData() {
		List<CpuUtilizationEntity> newCpuInfo = new ArrayList<>();
		LocalDate startDate = LocalDate.of(2024, 5, 1);
		LocalDate endDate = LocalDate.of(2024, 5, 11);

		// 2024년 5월 1일 부터 2024년 5월 5일까지의 모든 데이터 조회
		// 일단위 조회는 일 구간에서의 일단위로 조회한다.
		// 예를 들어 2024년 5월 1일 부터 2024년 5월 5일 까지 의 최소 , 최대, 평균 사용률 조회

		// 테스트로 5월 1일부터 10일까지 각 3개의 데이터를 i+3개씩 삽입한다.
		// 예를들어
		// 5월 1일 1시 1분, 5월 1일 2시 2분, 5월 1일 3시 3분 (3,4,5)
		// 5월 2일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (4,5,6)
		// 5월 3일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (5,6,7)
		// 5월 4일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (6,7,8)
		// 5월 5일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (7,8,9)
		// 5월 6일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (8,9,10)
		// 5월 7일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (9,10,11)
		// 5월 8일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (10,11,12)
		// 5월 9일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시 4분 (11,12,13)
		// 5월 10일 2시 2분, 5월 1일 2시 3분, 5월 1일 3시4분 (12,13,14)

		for (int i = 1; i <= 10; i++) {
			newCpuInfo.add(new CpuUtilizationEntity(i + 2, LocalDateTime.of(2024, 5, i, i, i)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 3, LocalDateTime.of(2024, 5, i, i, i + 1)));
			newCpuInfo.add(new CpuUtilizationEntity(i + 4, LocalDateTime.of(2024, 5, i, i, i + 2)));
		}

		List<CpuUtilizationEntity> tempResult = cpuUtilizationRepository.saveAll(newCpuInfo);

		// 5월 1일 10일까지 총 30개 데이터가 삽입되었다.
		// 위의 데이터 기반으로최소 사용율은 i+2 임으로 1일은 3, 2일은 4, 3일은 5......10일은 12 임을 알 수 있다.
		// 최대 사용율은 i+4 임으로 1일은 5,  2일은 6, 3일은 7......10일은 14 임을 알 수 있다.
		// 바탕으로하여 테스트

		List<SearchDayQueryDto> results = cpuUtilizationRepository.searchDateData(startDate, endDate);

		assertThat(results.size()).isEqualTo(10);

		for (int i = 0; i < 9; i++) {
			assertThat(results.get(i).getDay()).isEqualTo(i + 1);
			assertThat(results.get(i).getMin()).isEqualTo(i + 3);
			assertThat(results.get(i).getMax()).isEqualTo(i + 5);
		}

	}

}