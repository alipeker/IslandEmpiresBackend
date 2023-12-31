package com.islandempires.resourcesservice;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebFlux
@EnableDiscoveryClient
public class ResourcesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourcesServiceApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
