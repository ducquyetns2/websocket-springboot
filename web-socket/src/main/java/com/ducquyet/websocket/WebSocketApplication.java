package com.ducquyet.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WebSocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebSocketApplication.class, args);
		System.out.println("Server is running");
	}
}
