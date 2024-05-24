package com.international.cpuutilization.domain.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
	private final DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
 	private final 	NumberTemplate<Integer> minutePath = Expressions.numberTemplate(Integer.class, "extract(minute from {0})", cpuEntity.createdDate);
	private final NumberTemplate<Integer> hourPath = Expressions.numberTemplate(Integer.class, "extract(hour from {0})", cpuEntity.createdDate);
	//sql 문
	private final DateTemplate<LocalDate> date = Expressions.dateTemplate(LocalDate.class, "date_format({0}, {1})", cpuEntity.createdDate,"%Y-%m-%d");
	//h2
	private final DateTemplate<LocalDate> datePath = Expressions.dateTemplate(LocalDate.class, "FORMATDATETIME({0}, {1})", cpuEntity.createdDate,"yyyy-MM-dd");
	private final NumberTemplate<Integer> yearPath = Expressions.numberTemplate(Integer.class, "extract(year from {0})", cpuEntity.createdDate);
	private final NumberTemplate<Integer> monthPath = Expressions.numberTemplate(Integer.class, "extract(month from {0})", cpuEntity.createdDate);
	private final NumberTemplate<Integer> dayPath = Expressions.numberTemplate(Integer.class, "extract(day from {0})", cpuEntity.createdDate);

}
