package com.clienttrack.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ClienttrackApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClienttrackApiApplication.class, args);
	}
}
