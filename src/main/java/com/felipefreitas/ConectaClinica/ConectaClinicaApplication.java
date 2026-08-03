package com.felipefreitas.ConectaClinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ConectaClinicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConectaClinicaApplication.class, args);
	}

}
