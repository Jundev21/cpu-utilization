package com.international.cpuutilization.common;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.international.cpuutilization.domain.entity.QCpuUtilizationEntity;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;

import lombok.Getter;

@Getter
@Component
public class QueryDslExpressions {
	private final QCpuUtilizationEntity cpuEntity = QCpuUtilizationEntity.cpuUtilizationEntity;
	private final DateTemplate<LocalDate> date = Expressions.dateTemplate(LocalDate.class, "date_format({0}, {1})",
		cpuEntity.createdDate, "%Y-%m-%d");
	private final NumberTemplate<Integer> yearPath = Expressions.numberTemplate(Integer.class, "YEAR({0})",
		cpuEntity.createdDate);
	private final NumberTemplate<Integer> monthPath = Expressions.numberTemplate(Integer.class, "MONTH({0})",
		cpuEntity.createdDate);
	private final NumberTemplate<Integer> dayPath = Expressions.numberTemplate(Integer.class, "DAY({0})",
		cpuEntity.createdDate);
	private final NumberTemplate<Integer> hourPath = Expressions.numberTemplate(Integer.class, "HOUR({0})",
		cpuEntity.createdDate);
	private final NumberTemplate<Integer> minutePath = Expressions.numberTemplate(Integer.class, "MINUTE({0})",
		cpuEntity.createdDate);
}
