package com.islandempires.buildingworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableScheduling
public class BuildingworkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildingworkerApplication.class, args);
	}

}
