package com.islandempires.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.islandempires.gateway.config"})
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GatewayApplication.class);
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		// Configure allowed origins, methods, headers, etc. as needed
		config.addAllowedOrigin("*");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}

}
