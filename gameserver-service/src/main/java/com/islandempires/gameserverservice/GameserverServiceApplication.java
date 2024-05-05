package com.islandempires.gameserverservice;

import com.islandempires.gameserverservice.kafka.KafkaOutboxProducerService;;
import com.islandempires.gameserverservice.outbox.IslandOutboxEntityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.util.stream.Collectors;


@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients
@EnableWebFlux
@EnableReactiveFeignClients
@EnableScheduling
public class GameserverServiceApplication {

	@Autowired
	private KafkaOutboxProducerService kafkaOutboxProducerService;

	@Autowired
	private IslandOutboxEntityService islandOutboxEntityService;

	public static void main(String[] args) {
		SpringApplication.run(GameserverServiceApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Bean
	@ConditionalOnMissingBean
	public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
		return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
	}



	@Scheduled(fixedRateString ="10000")
	public void scheduleFixedRateTask() {
		/*
		CompletableFuture<Void> future =  this.kafkaOutboxProducerService.sendDeleteIslandMessage("ss");
		future.join();*/
		//islandOutboxEntityService.saveDeleteIslandEvent("6632d9abb1f71125540f2123").block();
	}

}
