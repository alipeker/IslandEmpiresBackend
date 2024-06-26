package com.islandempires.gameserverservice.filter;

import com.islandempires.gameserverservice.filter.client.WhoAmIClient;
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

    @Autowired
    private UrlValidator urlValidator;

    @Bean
    public WebFilter jwtWebFilter() {
        return (exchange, chain) -> {
            if(!urlValidator.isSecured.test(exchange.getRequest())) {
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
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }
        };
    }
}