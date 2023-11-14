package com.server.quant_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class QuantBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantBotApplication.class, args);
	}

}
