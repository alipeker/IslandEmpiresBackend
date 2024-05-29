package com.islandempires.militaryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableAutoConfiguration
public class MilitaryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilitaryServiceApplication.class, args);
	}

}
