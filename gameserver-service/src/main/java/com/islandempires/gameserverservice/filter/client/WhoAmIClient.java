package com.islandempires.gameserverservice.filter.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WhoAmIClient {

    private final WebClient gatewayWebClient;

    @Autowired
    public WhoAmIClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                        @Value("${urls.gateway}") String gatewayUrl) {
        this.gatewayWebClient = webClientBuilder
                                            .baseUrl(gatewayUrl)
                                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Mono<Long> whoami(String jwtToken) {
        return gatewayWebClient.get()
                .uri("/auth/me")
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnError(e -> Mono.error(e));
    }

}
