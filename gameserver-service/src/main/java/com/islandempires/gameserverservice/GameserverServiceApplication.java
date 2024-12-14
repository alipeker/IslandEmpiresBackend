package com.islandempires.gameserverservice;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

/*
	@Override
	public void run(String... args) throws Exception {
		List<SoldierSubTypeEnum> shipTypes = new ArrayList<>();
		shipTypes.add(SoldierSubTypeEnum.HOLK);
		shipTypes.add(SoldierSubTypeEnum.GUN_HOLK);
		shipTypes.add(SoldierSubTypeEnum.CARRACK);
		gameServerSoldierRepository.findAll().collectList().block().forEach(gameServerSoldier -> {
			gameServerSoldier.getSoldierBaseInfoList().forEach(soldierBaseInfo -> {
				soldierBaseInfo.setProductionDuration(Duration.ofMinutes(3));
				if(shipTypes.contains(soldierBaseInfo.getId())) {
					soldierBaseInfo.setSoldierCapacityOfShip(100);
					soldierBaseInfo.setCanonCapacityOfShip(10);
					soldierBaseInfo.setTotalLootCapacity(1000);
					soldierBaseInfo.setTimeToTraverseMapCell(Duration.ofMinutes(1));
					soldierBaseInfo.setProductionDuration(Duration.ofMinutes(3));
				}
			});
			gameServerSoldierRepository.save(gameServerSoldier).block();
		});
	}*/
}
