package com.sdc.callingapp.tripcarmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.sdc.callingapp.tripcarmanagement")
public class TripCarManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripCarManagementApplication.class, args);
	}
}
