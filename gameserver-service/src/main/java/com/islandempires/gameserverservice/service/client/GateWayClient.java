package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.model.building.AllBuildings;
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

    public IslandBuildingDTO initializeIslandBuildings2(String islandId, AllBuildings allBuildingList, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        HttpEntity<AllBuildings> requestEntity = new HttpEntity<>(allBuildingList, headers);

        ResponseEntity<IslandBuildingDTO> responseEntity = restTemplate.exchange(this.gatewayUrl + "/building/" + islandId, HttpMethod.POST, requestEntity, IslandBuildingDTO.class);
        return responseEntity.getBody();
    }

    public IslandResourceDTO initializeIslandResource2(IslandResourceDTO initialIslandResourceDTO, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);

        HttpEntity<IslandResourceDTO> requestEntity = new HttpEntity<>(initialIslandResourceDTO, headers);

        ResponseEntity<IslandResourceDTO> responseEntity = restTemplate.exchange(this.gatewayUrl + "/resource/", HttpMethod.POST, requestEntity, IslandResourceDTO.class);
        return responseEntity.getBody();
    }

    public IslandDTO initializeIsland2(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<IslandDTO> responseEntity = restTemplate.exchange(this.gatewayUrl + "/island/", HttpMethod.POST, requestEntity, IslandDTO.class);
        return responseEntity.getBody();
    }


}

