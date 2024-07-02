package com.islandempires.islandservice.service.client;

import com.islandempires.islandservice.dto.initial.InitialAllBuildingsDTO;
import com.islandempires.islandservice.dto.initial.InitialIslandResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public Mono<InitialAllBuildingsDTO> initializeIslandBuildings(String islandId, InitialAllBuildingsDTO allBuildingList, Long userId, String serverId) {
        return gatewayWebClient.post()
                .uri("/building/private/{serverId}/{islandId}/{userId}", serverId, islandId, userId)
                .bodyValue(allBuildingList)
                .retrieve()
                .bodyToMono(InitialAllBuildingsDTO.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<InitialIslandResourceDTO> initializeIslandResource(String islandId, InitialIslandResourceDTO initialIslandResourceDTO, Long userId, String serverId) {
        return gatewayWebClient.post()
                .uri("/resource/private/{serverId}/{islandId}/{userId}", serverId, islandId, userId)
                .bodyValue(initialIslandResourceDTO)
                .retrieve()
                .bodyToMono(InitialIslandResourceDTO.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<Object> initializeIslandMilitary(String islandId, Long userId, String serverId) {
        return gatewayWebClient.post()
                .uri("/military/private/{userId}/{serverId}/{islandId}", userId, serverId, islandId)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnError(e -> Mono.error(e));
    }


}

