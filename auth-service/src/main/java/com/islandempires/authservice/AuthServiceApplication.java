package com.islandempires.authservice;

import com.islandempires.authservice.model.AppUser;
import com.islandempires.authservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.islandempires.authservice.repository", "com.islandempires.authservice.service",
		"com.islandempires.authservice.controller", "com.islandempires.authservice.security",
		"com.islandempires.authservice.exception"})
public class AuthServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


}
