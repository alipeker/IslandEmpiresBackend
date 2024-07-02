package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.island.IslandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;

@Component
public class GateWayClient {

    private final WebClient gatewayWebClient;

    @Value("${urls.gateway}")
    private String gatewayUrl;

    @Autowired
    public GateWayClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                         @Value("${urls.gateway}") String gatewayUrl) {
        this.gatewayWebClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Mono<IslandDTO> initializeIsland(InitialGameServerPropertiesDTO initialGameServerPropertiesDTO,
                                            String serverId, Long userId) {
        return gatewayWebClient.post()
                .uri("/island/private/{serverId}/{userId}", serverId, userId)
                .bodyValue(initialGameServerPropertiesDTO)
                .retrieve()
                .bodyToMono(IslandDTO.class)
                .doOnError(e -> Mono.error(e));
    }


}

