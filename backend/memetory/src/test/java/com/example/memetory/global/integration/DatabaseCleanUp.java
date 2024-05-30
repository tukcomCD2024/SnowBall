package com.example.memetory.global.integration;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;

@Service
@Profile("test")
public class DatabaseCleanUp implements InitializingBean {

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tables;

	@Override
	public void afterPropertiesSet() {
		tables = entityManager.getMetamodel()
			.getEntities()
			.stream()
			.filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
			.map(this::getTableNameOrElseGetEntityName)
			.toList();
	}

	private String getTableNameOrElseGetEntityName(EntityType<?> e) {
		Table table = e.getJavaType().getAnnotation(Table.class);
		if (table != null && !table.name().isEmpty()) {
			return table.name();
		} else {
			return e.getName().toLowerCase();
		}
	}

	@Transactional
	public void execute() {
		entityManager.flush();

		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
		for (String table : tables) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
		}

		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
	}
}