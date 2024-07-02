package com.islandempires.islandservice.filter;

import com.islandempires.islandservice.filter.client.WhoAmIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
public class FilterConfig {

    @Autowired
    private WhoAmIClient whoAmIClient;

    @Bean
    public WebFilter jwtWebFilter() {
        return (exchange, chain) -> {
            if(exchange.getRequest().getPath().toString().startsWith("/island/private")) {
                return chain.filter(exchange);
            }
            String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                return whoAmIClient.whoami(jwtToken).flatMap(userId -> {
                    exchange.getAttributes().put("userId", userId);
                    return Mono.just(userId);
                }).then(chain.filter(exchange));
            } else {
                // JWT token bulunamadıysa isteği reddet
                exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return Mono.empty();
            }
        };
    }
}