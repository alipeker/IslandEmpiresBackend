package com.islandempires.militaryservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableAutoConfiguration
@EntityScan(basePackages = "com.islandempires.militaryservice.model")
@EnableJpaRepositories
public class MilitaryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MilitaryServiceApplication.class, args);
	}


	@Bean
	public WebClient.Builder gatewayClient() {
		return WebClient.builder();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
