package com.islandempires.buildingworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableScheduling
@SpringBootApplication
public class BuildingworkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildingworkerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


	@Bean
	public WebClient.Builder gatewayWebClientBuilder() {
		return WebClient.builder();
	}
}