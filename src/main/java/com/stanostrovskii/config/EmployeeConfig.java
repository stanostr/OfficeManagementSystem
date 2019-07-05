package com.stanostrovskii.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stanostrovskii.dao.AdminRepository;
import com.stanostrovskii.dao.EmployeeRepository;
import com.stanostrovskii.model.Admin;
import com.stanostrovskii.model.Employee;

@Configuration
public class EmployeeConfig {
	/**
	 * Load some employees for testing
	 */
	@Bean
	public CommandLineRunner loadEmployees(EmployeeRepository repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) {
				try {
					repo.save(new Employee("Sven", "Ostrovskii", "sven@valhalla.com", "1234567890", "$2a$10$5kCrzm.hQBEmtBheogcxEutW2bgbfS4WsOBlrXSAKIng..cVVQ.Hm"));
					repo.save(new Employee("Olaf", "Ostrovskii", "olaf@valhalla.com", "0987654321", "$2a$10$tOnY8XNzq1T2CFIuHqQE5eN1ul/FXaBVtFdesFDY8M5MVeQ6rojUe"));
				} catch (Exception e) {
					// do nothing if we fail for any reason
				}
			}
		};
	}
}
