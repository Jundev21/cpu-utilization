package com.international.cpuutilization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.international.cpuutilization.common.QueryDslExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;
	@Bean
	public JPAQueryFactory jpaQueryFactory(){
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public QueryDslExpressions queryDslExpressions() {
		return new QueryDslExpressions();
	}

}
