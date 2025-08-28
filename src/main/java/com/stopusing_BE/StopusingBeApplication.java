package com.stopusing_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StopusingBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StopusingBeApplication.class, args);
	}

}
