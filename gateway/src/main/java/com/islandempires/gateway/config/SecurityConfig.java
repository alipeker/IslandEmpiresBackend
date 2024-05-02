package com.islandempires.gateway.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain configureResourceServer(ServerHttpSecurity httpSecurity) {

        return httpSecurity
                .authorizeExchange()
                .pathMatchers(
                        "/actuator/health/**",
                        "/auth/**"
                ).permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfiguration())
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .oauth2ResourceServer().jwt()
                .and()
                .and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(List.of("*"));
        corsConfig.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}