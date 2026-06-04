package com.fullstack.SoftwareAviones.msvuelo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// Hablar con profesor
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsvueloApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvueloApplication.class, args);
	}

	// Hablar con profesor
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



}
