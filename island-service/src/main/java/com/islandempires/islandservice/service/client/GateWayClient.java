package com.islandempires.islandservice.service.client;

import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import com.islandempires.islandservice.dto.initial.InitialAllBuildingsDTO;
import com.islandempires.islandservice.dto.initial.InitialIslandResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GateWayClient {

    private final WebClient gatewayWebClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${urls.gateway}")
    private String gatewayUrl;

    @Autowired
    public GateWayClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                         @Value("${urls.gateway}") String gatewayUrl) {
        this.gatewayWebClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Mono<InitialAllBuildingsDTO> initializeIslandBuildings(String islandId, InitialAllBuildingsDTO allBuildingList, String jwtToken) {
        return gatewayWebClient.post()
                .uri("/building/{islandId}", islandId)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .bodyValue(allBuildingList)
                .retrieve()
                .bodyToMono(InitialAllBuildingsDTO.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<InitialIslandResource> initializeIslandResource(String islandId, InitialIslandResource initialIslandResourceDTO, String jwtToken) {
        return gatewayWebClient.post()
                .uri("/resource/{islandId}", islandId)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .bodyValue(initialIslandResourceDTO)
                .retrieve()
                .bodyToMono(InitialIslandResource.class)
                .doOnError(e -> Mono.error(e));
    }


}

