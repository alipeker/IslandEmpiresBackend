package com.islandempires.gameserverservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


@SpringBootApplication
@EnableAutoConfiguration
@EnableFeignClients
@EnableWebFlux
@EnableReactiveFeignClients
public class GameserverServiceApplication {

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
}
