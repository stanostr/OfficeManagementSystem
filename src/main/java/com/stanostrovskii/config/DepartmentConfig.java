package com.stanostrovskii.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stanostrovskii.dao.DepartmentRepository;
import com.stanostrovskii.model.Department;

@Configuration
public class DepartmentConfig {

	/**
	 * Load some departments for testing
	 */
	@Bean
	public CommandLineRunner loadDepartments(DepartmentRepository repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) {
				try {
					repo.save(new Department("HR"));
					repo.save(new Department("IT"));
					repo.save(new Department("Transportation"));
					repo.save(new Department("Defense"));
				} catch (Exception e) {
					// do nothing if we fail for any reason
				}
			}
		};
	}
}
