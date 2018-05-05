package com.sdc.callingapp.tripcarmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan("com.sdc.callingapp.tripcarmanagement")
public class TripCarManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripCarManagementApplication.class, args);
	}
}
