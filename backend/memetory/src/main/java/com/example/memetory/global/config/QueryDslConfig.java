package com.example.memetory.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Configuration
public class QueryDslConfig {
	@Configuration
	public class QuerydslConfig {
		@Bean
		public JPAQueryFactory jpaQueryFactory(final EntityManager entityManager) {
			return new JPAQueryFactory(entityManager);
		}
	}
}
