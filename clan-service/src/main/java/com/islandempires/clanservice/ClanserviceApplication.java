package com.islandempires.clanservice;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.islandempires.clanservice", "com.islandempires.clanservice.filter", "com.islandempires.clanservice.outbox"})
@EnableDiscoveryClient
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.islandempires.clanservice.model"})
@EnableScheduling
@EnableJpaRepositories
@EnableFeignClients
@EnableKafka
public class ClanserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClanserviceApplication.class, args);
	}

	@Bean
	public Hibernate5JakartaModule hibernateModule() {
		return new Hibernate5JakartaModule();
	}
}
