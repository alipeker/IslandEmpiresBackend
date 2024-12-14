package com.islandempires.resourcesservice.filter.client;

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

    private final WebClient islandResourceWebClient;

    @Autowired
    public WhoAmIClient(@Qualifier("islandResourceWebClientBuilder") WebClient.Builder webClientBuilder,
                        @Value("${urls.gateway}") String gatewayUrl) {
        System.out.println(gatewayUrl);
        this.islandResourceWebClient = webClientBuilder
                                            .baseUrl(gatewayUrl)
                                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Mono<Long> whoami(String jwtToken) {
        return islandResourceWebClient.get()
                .uri("/auth/me")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<Double> getDistanceBetweenIslands(String islandId1, String islandId2) {
        return islandResourceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/island/private/calculateDistance/{islandId1}/{islandId2}")
                        .build(islandId1, islandId2))
                .retrieve()
                .bodyToMono(Double.class);
    }

    public Mono<Boolean> checkUsersSameClan(String userId1, String userId2) {
        return islandResourceWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/clan/private/checkUsersSameClan/{userId1}/{userId2}")
                        .build(userId1, userId2))
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
