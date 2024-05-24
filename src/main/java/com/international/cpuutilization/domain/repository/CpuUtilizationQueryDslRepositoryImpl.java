package com.international.cpuutilization.domain.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.international.cpuutilization.domain.dto.response.SearchDateResponse;
import com.international.cpuutilization.domain.dto.response.SearchHourResponse;
import com.international.cpuutilization.domain.dto.response.SearchMinuteResponse;
import com.international.cpuutilization.domain.entity.QCpuUtilizationEntity;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CpuUtilizationQueryDslRepositoryImpl implements CpuUtilizationQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QCpuUtilizationEntity cpuEntity = QCpuUtilizationEntity.cpuUtilizationEntity;
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");

	@Override
	public List<SearchMinuteResponse> searchMinData(LocalDateTime startDate, LocalDateTime endDate) {

		NumberTemplate<Integer> minutePath = Expressions.numberTemplate(Integer.class, "extract(minute from {0})",
			cpuEntity.createdDate);
		NumberTemplate<Integer> hourPath = Expressions.numberTemplate(Integer.class, "extract(hour from {0})",
			cpuEntity.createdDate);
		List<SearchMinuteResponse> result = new ArrayList<>();

		List<Tuple> searchData = jpaQueryFactory.select(
				cpuEntity.createdDate,
				hourPath,
				minutePath,
				cpuEntity.cpuUtilization
			)
			.from(cpuEntity)
			.where(cpuEntity.createdDate.between(startDate, endDate))
			.fetch();

		for (int i = 0; i < 60; i++) {
			boolean hasMinuteData = false;
			for (Tuple tuple : searchData) {
				Integer getMinute = tuple.get(minutePath);
				Double getCpuUtilization = tuple.get(cpuEntity.cpuUtilization);

				if (getMinute != null && getCpuUtilization != null && getMinute == i) {
					SearchMinuteResponse newData = SearchMinuteResponse.builder()
						.dateTime(tuple.get(cpuEntity.createdDate).format(dtf))
						.minutes(getMinute)
						.currHours(getMinute)
						.currMinutes(i)
						.cpuUtilization(getCpuUtilization)
						.build();
					hasMinuteData = true;
					result.add(newData);
				}
			}
			if (!hasMinuteData) {
				SearchMinuteResponse newData = SearchMinuteResponse.builder()
					.dateTime("not exist")
					.minutes(0)
					.currHours(0)
					.currMinutes(i)
					.cpuUtilization(0.0)
					.build();
				result.add(newData);
			}
		}
		return result;
	}

	@Override
	public List<SearchHourResponse> searchHourData(LocalDateTime pickedDay) {

		NumberTemplate<Integer> hourPath = Expressions.numberTemplate(Integer.class, "extract(hour from {0})",
			cpuEntity.createdDate);

		List<SearchHourResponse> result = new ArrayList<>();

		List<Tuple> subResult = jpaQueryFactory.select(
				cpuEntity.createdDate,
				hourPath,
				cpuEntity.cpuUtilization.min(),
				cpuEntity.cpuUtilization.max(),
				cpuEntity.cpuUtilization.avg()
			)
			.from(cpuEntity)
			.where(cpuEntity.createdDate.eq(pickedDay))
			.groupBy(hourPath)
			.orderBy(hourPath.asc())
			.fetch();

		for (int i = 0; i < 24; i++) {
			boolean hasHourData = false;

			for (Tuple tuple : subResult) {
				Double min = tuple.get(cpuEntity.cpuUtilization.min());
				Double max = tuple.get(cpuEntity.cpuUtilization.max());
				Double avg = tuple.get(cpuEntity.cpuUtilization.avg());
				Integer getHour = tuple.get(hourPath);

				if (min != null && max != null && avg != null && getHour != null && getHour == i) {
					SearchHourResponse newData = SearchHourResponse.builder()
						.dateTime(tuple.get(cpuEntity.createdDate).format(dtf))
						.currentHour(getHour)
						.minimumUtilization(min)
						.maximumUtilization(max)
						.averageUtilization(avg)
						.build();

					result.add(newData);
				}
			}
			if (!hasHourData) {
				SearchHourResponse nonData = SearchHourResponse.builder()
					.dateTime("not exist")
					.currentHour(0)
					.minimumUtilization(0.0)
					.maximumUtilization(0.0)
					.averageUtilization(0.0)
					.build();
				result.add(nonData);
			}
		}
		return result;
	}

	@Override
	public List<SearchDateResponse> searchDateData(LocalDateTime startDate, LocalDateTime endDate) {

		NumberTemplate<Integer> dayTemplate = Expressions.numberTemplate(Integer.class, "extract(day from {0})",
			cpuEntity.createdDate);

		List<SearchDateResponse> result = new ArrayList<>();

		List<Tuple> searchData = jpaQueryFactory.select(
				cpuEntity.createdDate,
				dayTemplate,
				cpuEntity.cpuUtilization.min(),
				cpuEntity.cpuUtilization.max(),
				cpuEntity.cpuUtilization.avg()
			)
			.from(cpuEntity)
			.where(cpuEntity.createdDate.between(startDate, endDate))
			.groupBy(dayTemplate)
			.fetch();

		for(Tuple tuple : searchData) {
			Integer getDay = tuple.get(dayTemplate);
			Double min = tuple.get(cpuEntity.cpuUtilization.min());
			Double max = tuple.get(cpuEntity.cpuUtilization.max());
			Double avg = tuple.get(cpuEntity.cpuUtilization.avg());

			if (getDay != null && min != null && max !=null && avg !=null) {
				SearchDateResponse newData = SearchDateResponse.builder()
					.dateTime(tuple.get(cpuEntity.createdDate).format(dtf))
					.currentDay(getDay)
					.minimumUtilization(min)
					.maximumUtilization(max)
					.averageUtilization(avg)
					.build();
				result.add(newData);
			}
		}
		return result;
	}

}

// SQL Server에서 GROUP BY 절은 특정 칼럼을 기준으로 집계 함수를 사용하여 건수(COUNT), 합계(SUM), 평균(AVG) 등 집 계성 데이터를 추출할 때 사용한다.
// GROUP BY 절에서 기준 칼럼을 여러 개 지정할 수 있으며,
// HAVING 절을 함께 사용하면 집계 함수를 사용하여 WHERE 절의 조건절처럼 조건을 부여할 수 있다.
// GROUP BY 절은 중복제거를 할 때도 사용 가능하다.
// SQL Server에서는 GROUP BY 절을 사용할 경우 그룹 칼럼을 기준으로 자동으로 ORDER BY가 되지만 명시적으로 ORDER BY를 사용하여 쿼리문을 작성하는 것이 좋다.

// group by 절은 특정 칼럼을 기준으로 집계 함수 사용할때 사용한다.
// 집계함수는 count, sum, avg 등으로 데이터의 통계를 구하려 할때 사용한다.
//group by 절은 중복을 제거할때 사용.

// query dsl 사용 이유 jpa 는 jpql 을 주로 사용하지만 jpql 은 순수 스트링 형태로로 사용하기 때문에 타입이 언세이프 합니다.
// 그래서 타입이 세이프하며 집계함수를 사용 하기 위해서 query dsl 을 사용하게 되었다.
// query dsl 을 사용하면서 mysql 에서 제공하는 함수들을 사용하는게 한계가 있었다.