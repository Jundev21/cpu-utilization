package com.international.cpuutilization.domain.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.international.cpuutilization.domain.dto.QueryDto.SearchDayQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchHourQueryDto;
import com.international.cpuutilization.domain.dto.QueryDto.SearchMinuteQueryDto;
import com.international.cpuutilization.domain.entity.QCpuUtilizationEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CpuUtilizationQueryDslRepositoryImpl implements CpuUtilizationQueryDslRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QCpuUtilizationEntity cpuEntity = QCpuUtilizationEntity.cpuUtilizationEntity;
	private final QueryDslExpressions queryDslExpressions;

	@Override
	public List<SearchMinuteQueryDto> searchMinData(LocalDateTime startDate, LocalDateTime endDate) {
		return jpaQueryFactory.select(
				Projections.fields(
					SearchMinuteQueryDto.class,
					queryDslExpressions.getYearPath().as("year"),
					queryDslExpressions.getMonthPath().as("month"),
					queryDslExpressions.getDayPath().as("day"),
					queryDslExpressions.getHourPath().as("hour"),
					queryDslExpressions.getMinutePath().as("minute"),
					cpuEntity.cpuUtilization))
			.from(cpuEntity)
			.where(cpuEntity.createdDate.between(startDate, endDate))
			.groupBy(
				queryDslExpressions.getYearPath(),
				queryDslExpressions.getMonthPath(),
				queryDslExpressions.getDayPath(),
				queryDslExpressions.getHourPath(),
				queryDslExpressions.getMinutePath(),
				cpuEntity.cpuUtilization
			)
			.fetch();
	}

	@Override
	public List<SearchHourQueryDto> searchHourData(LocalDate pickedDay) {
		return jpaQueryFactory.select(
				Projections.fields(
					SearchHourQueryDto.class,
					queryDslExpressions.getYearPath().as("year"),
					queryDslExpressions.getMonthPath().as("month"),
					queryDslExpressions.getDayPath().as("day"),
					queryDslExpressions.getHourPath().as("hour"),
					cpuEntity.cpuUtilization.min().as("min"),
					cpuEntity.cpuUtilization.max().as("max"),
					cpuEntity.cpuUtilization.avg().as("avg")
				))
			.from(cpuEntity)
			.where(queryDslExpressions.getYearPath().eq(pickedDay.getYear()),
				queryDslExpressions.getMonthPath().eq(pickedDay.getMonthValue()),
				queryDslExpressions.getDayPath().eq(pickedDay.getDayOfMonth())
			)
			.groupBy(
				queryDslExpressions.getYearPath(),
				queryDslExpressions.getMonthPath(),
				queryDslExpressions.getDayPath(),
				queryDslExpressions.getHourPath())
			.fetch();
	}

	@Override
	public List<SearchDayQueryDto> searchDateData(LocalDate startDate, LocalDate endDate) {
		return jpaQueryFactory.select(
				Projections.fields(
					SearchDayQueryDto.class,
					queryDslExpressions.getYearPath().as("year"),
					queryDslExpressions.getMonthPath().as("month"),
					queryDslExpressions.getDayPath().as("day"),
					cpuEntity.cpuUtilization.min().as("min"),
					cpuEntity.cpuUtilization.max().as("max"),
					cpuEntity.cpuUtilization.avg().as("avg")
				))
			.from(cpuEntity)
			.where(cpuEntity.createdDate.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
			.groupBy(
				queryDslExpressions.getYearPath(),
				queryDslExpressions.getMonthPath(),
				queryDslExpressions.getDayPath()
			)
			.fetch();
	}

	// @Override
	// public List<SearchMinuteResponse> searchMinData(LocalDateTime startDate, LocalDateTime endDate) {
	//
	//
	//
	// 	List<Tuple> searchData = jpaQueryFactory.select(
	// 			cpuEntity.createdDate,
	// 			queryDslExpressions.getHourPath(),
	// 			queryDslExpressions.getMinutePath(),
	// 			cpuEntity.cpuUtilization
	// 		)
	// 		.from(cpuEntity)
	// 		.where(cpuEntity.createdDate.between(startDate, endDate))
	// 		.fetch();
	//
	// 	for (int i = 0; i < 60; i++) {
	// 		boolean hasMinuteData = false;
	// 		for (Tuple tuple : searchData) {
	// 			Integer getMinute = tuple.get(queryDslExpressions.getMinutePath());
	// 			Double getCpuUtilization = tuple.get(cpuEntity.cpuUtilization);
	//
	// 			if (getMinute != null && getCpuUtilization != null && getMinute == i) {
	// 				SearchMinuteResponse newData = SearchMinuteResponse.builder()
	// 					.dateTime(tuple.get(cpuEntity.createdDate).format(queryDslExpressions.getDtf()))
	// 					.minutes(getMinute)
	// 					.currHours(getMinute)
	// 					.currMinutes(i)
	// 					.cpuUtilization(getCpuUtilization)
	// 					.build();
	// 				hasMinuteData = true;
	// 				result.add(newData);
	// 			}
	// 		}
	// 		if (!hasMinuteData) {
	// 			SearchMinuteResponse newData = SearchMinuteResponse.builder()
	// 				.dateTime("not exist")
	// 				.minutes(0)
	// 				.currHours(0)
	// 				.currMinutes(i)
	// 				.cpuUtilization(0.0)
	// 				.build();
	// 			result.add(newData);
	// 		}
	// 	}
	// 	return result;
	// }

	// @Override
	// public List<SearchHourResponse> searchHourData(LocalDate pickedDay) {
	// 	List<SearchHourResponse> result = new ArrayList<>();
	// 	List<Tuple> subResult = jpaQueryFactory.select(
	// 			queryDslExpressions.getHourPath(),
	// 			cpuEntity.cpuUtilization.min(),
	// 			cpuEntity.cpuUtilization.max(),
	// 			cpuEntity.cpuUtilization.avg()
	// 		)
	// 		.from(cpuEntity)
	// 		.where(queryDslExpressions.getDatePath().eq(pickedDay))
	// 		.groupBy(queryDslExpressions.getHourPath())
	// 		.fetch();
	//
	// 	for (int i = 0; i < 24; i++) {
	// 		boolean hasHourData = false;
	// 		for (Tuple tuple : subResult) {
	// 			Double min = tuple.get(cpuEntity.cpuUtilization.min());
	// 			Double max = tuple.get(cpuEntity.cpuUtilization.max());
	// 			Double avg = tuple.get(cpuEntity.cpuUtilization.avg());
	// 			Integer getHour = tuple.get(queryDslExpressions.getHourPath());
	//
	// 			if (min != null && max != null && avg != null && getHour != null && getHour == i) {
	// 				SearchHourResponse newData = SearchHourResponse.builder()
	// 					.dateTime(pickedDay.format(queryDslExpressions.getDt()))
	// 					.currentHour(getHour)
	// 					.countHour(i)
	// 					.minimumUtilization(min)
	// 					.maximumUtilization(max)
	// 					.averageUtilization(avg)
	// 					.build();
	// 				hasHourData = true;
	// 				result.add(newData);
	// 			}
	// 		}
	// 		if (!hasHourData) {
	// 			SearchHourResponse nonData = SearchHourResponse.builder()
	// 				.dateTime(pickedDay.format(queryDslExpressions.getDt()))
	// 				.currentHour(0)
	// 				.countHour(i)
	// 				.minimumUtilization(0.0)
	// 				.maximumUtilization(0.0)
	// 				.averageUtilization(0.0)
	// 				.build();
	// 			result.add(nonData);
	// 		}
	// 	}
	// 	return result;
	// }

	// @Override
	// public List<SearchDateResponse> searchDateData(LocalDateTime startDate, LocalDateTime endDate) {
	//
	// 	List<SearchDateResponse> result = new ArrayList<>();
	//
	// 	List<Tuple> searchData = jpaQueryFactory.select(
	// 			queryDslExpressions.getYearPath(),
	// 			queryDslExpressions.getMonthPath(),
	// 			queryDslExpressions.getDayPath(),
	// 			cpuEntity.cpuUtilization.min(),
	// 			cpuEntity.cpuUtilization.max(),
	// 			cpuEntity.cpuUtilization.avg()
	// 		)
	// 		.from(cpuEntity)
	// 		.where(cpuEntity.createdDate.between(startDate, endDate))
	// 		.groupBy(queryDslExpressions.getYearPath(), queryDslExpressions.getMonthPath(),
	// 			queryDslExpressions.getDayPath())
	// 		.fetch();
	//
	// 	LocalDate copyStartDate = startDate.toLocalDate();;
	// 	LocalDate copyEndDate = endDate.toLocalDate();;
	//
	// 	while (copyStartDate.compareTo(copyEndDate) < 1) {
	// 		boolean hasDayData = false;
	//
	// 		for (Tuple tuple : searchData) {
	// 			Double min = tuple.get(cpuEntity.cpuUtilization.min());
	// 			Double max = tuple.get(cpuEntity.cpuUtilization.max());
	// 			Double avg = tuple.get(cpuEntity.cpuUtilization.avg());
	//
	// 			LocalDate localDateFomatted = LocalDate.of(
	// 				tuple.get(queryDslExpressions.getYearPath()),
	// 				tuple.get(queryDslExpressions.getMonthPath()),
	// 				tuple.get(queryDslExpressions.getDayPath()));
	//
	// 			if (min != null && max != null && avg != null && copyStartDate.isEqual(localDateFomatted)) {
	// 				SearchDateResponse newData = SearchDateResponse.builder()
	// 					.dateTime(localDateFomatted.format(queryDslExpressions.getDt()))
	// 					.minimumUtilization(min)
	// 					.maximumUtilization(max)
	// 					.averageUtilization(avg)
	// 					.build();
	// 				hasDayData=true;
	// 				result.add(newData);
	// 			}
	// 		}
	//
	// 		if (!hasDayData) {
	// 			SearchDateResponse nonData = SearchDateResponse.builder()
	// 				.dateTime(copyStartDate.format(queryDslExpressions.getDt()))
	// 				.minimumUtilization(0.0)
	// 				.maximumUtilization(0.0)
	// 				.averageUtilization(0.0)
	// 				.build();
	// 			result.add(nonData);
	// 		}
	// 		copyStartDate = copyStartDate.plusDays(1);
	// 	}
	//
	// 	return result;
	// }

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

// SQL Function 을 사용해야하는 경우가 있다. 그러기 위해서는 Expression 을 사용하면 sql function 을 사용할 수 있다.
