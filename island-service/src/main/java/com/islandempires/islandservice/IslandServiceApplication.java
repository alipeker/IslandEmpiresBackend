package com.islandempires.islandservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.islandempires.islandservice.repository", "com.islandempires.islandservice.service",
		"com.islandempires.islandservice.controller", "com.islandempires.islandservice.exception", "com.islandempires.islandservice.kafka",
		"com.islandempires.islandservice.outbox"})
@EnableKafka
public class IslandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IslandServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
