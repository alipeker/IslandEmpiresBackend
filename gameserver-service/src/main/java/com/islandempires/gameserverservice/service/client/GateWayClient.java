package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GateWayClient {

    private final WebClient gatewayWebClient;

    @Autowired
    public GateWayClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                         @Value("${urls.gateway}") String gatewayUrl) {
        this.gatewayWebClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Mono<IslandBuildingDTO> initializeIslandBuildings(String islandId, AllBuildings allBuildingList, String jwtToken) {
        return gatewayWebClient.post()
                .uri("/building/{islandId}", islandId)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .bodyValue(allBuildingList)
                .retrieve()
                .bodyToMono(IslandBuildingDTO.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<IslandResourceDTO> initializeIslandResource(IslandResourceDTO initialIslandResourceDTO, String jwtToken) {
        return gatewayWebClient.post()
                .uri("/resource/")
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .bodyValue(initialIslandResourceDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(e -> Mono.error(e));
    }

    public Mono<IslandDTO> initializeIsland(String jwtToken) {
        return gatewayWebClient.post()
                .uri("/island/")
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .retrieve()
                .bodyToMono(IslandDTO.class)
                .doOnError(e -> Mono.error(e));
    }
}

