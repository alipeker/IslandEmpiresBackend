package com.islandempires.resourcesservice.filter;

import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class FilterConfig {

    @Autowired
    private WhoAmIClient whoAmIClient;

    @Bean
    public WebFilter jwtWebFilter() {
        return (exchange, chain) -> {
            String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                return whoAmIClient.whoami(jwtToken).flatMap(userId -> {
                    exchange.getAttributes().put("userId", userId);
                    return Mono.just(userId);
                }).then(chain.filter(exchange));
            } else {

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }
        };
    }
}