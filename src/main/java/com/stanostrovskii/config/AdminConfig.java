package com.stanostrovskii.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stanostrovskii.dao.AdminRepository;
import com.stanostrovskii.model.Admin;

@Configuration
public class AdminConfig {
	/**
	 * Load some admins for testing
	 */
	@Bean
	public CommandLineRunner loadAdministrators(AdminRepository repo) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) {
				try {
					repo.save(new Admin("Stan", "Ostrovskii", "admin1", "$2a$10$5kCrzm.hQBEmtBheogcxEutW2bgbfS4WsOBlrXSAKIng..cVVQ.Hm"));
					repo.save(new Admin("Silky", "Ostrovskii", "admin2", "$2a$10$tOnY8XNzq1T2CFIuHqQE5eN1ul/FXaBVtFdesFDY8M5MVeQ6rojUe"));
				} catch (Exception e) {
					// do nothing if we fail for any reason
				}
			}
		};
	}
}
