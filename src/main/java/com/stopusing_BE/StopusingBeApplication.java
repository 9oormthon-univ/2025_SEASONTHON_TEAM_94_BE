package com.stopusing_BE;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StopusingBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StopusingBeApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
