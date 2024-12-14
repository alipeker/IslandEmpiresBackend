package com.islandempires.buildingservice.shared.service.client;

import com.islandempires.buildingservice.shared.BuildingServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GameServerClient {

    private final WebClient islandResourceWebClient;

    @Autowired
    public GameServerClient(@Qualifier("gatewayClient") WebClient.Builder webClientBuilder,
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

    public Flux<BuildingServerProperties> getGameServerBuildingProperties() {
        return islandResourceWebClient.get()
                .uri("/gameservice/private/getGameServerBuildingProperties")
                .retrieve()
                .bodyToFlux(BuildingServerProperties.class)
                .onErrorResume(e -> {
                    return Flux.error(e);
                });
    }
}
