package com.islandempires.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final RestTemplate restTemplate;

    public SecurityConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public SecurityWebFilterChain configureResourceServer(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeExchange()
                .pathMatchers(
                        "/actuator/health/**",
                        "/auth/**", "/resource/**"
                ).permitAll()
                .anyExchange().authenticated()
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfiguration())
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .addFilterBefore(authenticationJwtTokenFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public WebFilter authenticationJwtTokenFilter() {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            try {
                Long userid = getUserId(headers.get("Authorization").get(0));
                exchange.getRequest().mutate().headers(httpHeaders -> {
                    if(!httpHeaders.containsKey("userid")) {
                        httpHeaders.add("userid", userid.toString());
                        return;
                    }
                });
            } catch (Exception E) {

            }
            // Add your custom header
            return chain.filter(exchange);
        };
    }

    public Long getUserId(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Test", "test");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponseDTO> responseEntity = restTemplate.exchange("http://192.168.0.14:1001/auth/me", HttpMethod.GET, requestEntity, UserResponseDTO.class);

        // Process the response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody().getId();
        } else {
            System.err.println("Failed to fetch data. Status code: " + responseEntity.getStatusCodeValue());
            throw new RuntimeException();
        }
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