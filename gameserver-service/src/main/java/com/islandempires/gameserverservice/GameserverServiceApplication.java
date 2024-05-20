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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.util.stream.Collectors;


@SpringBootApplication
@EnableAutoConfiguration
@EnableWebFlux
@EnableScheduling
@EnableRedisRepositories
@EnableReactiveMongoRepositories(basePackages = "com.islandempires.gameserverservice.repository",
includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION))
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

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// Configure LettuceConnectionFactory with the appropriate settings
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory("localhost", 6380);
		// You can set other properties like password, timeout, etc. if needed
		return lettuceConnectionFactory;
	}


}
