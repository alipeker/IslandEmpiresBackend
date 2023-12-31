package com.islandempires.resourceworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class ResourceworkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceworkerApplication.class, args);
	}

}
