package com.islandempires.sessiontrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableAutoConfiguration
@EnableScheduling
public class SessionTrackingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionTrackingServiceApplication.class, args);
	}

}
