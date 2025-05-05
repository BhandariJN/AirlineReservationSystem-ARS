package com.AirlineReservationSystem_ARS.AirlineReservationSystem_ARS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableScheduling
@SpringBootApplication
public class AirlineReservationSystemArsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineReservationSystemArsApplication.class, args);
	}

}
