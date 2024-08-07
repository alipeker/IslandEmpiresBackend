package com.islandempires.buildingservice.service.client;

import com.islandempires.buildingservice.dto.IslandResourceDTO;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IslandResourceWebClientNew {

    private final WebClient islandResourceWebClient;

    @Autowired
    public IslandResourceWebClientNew(@Qualifier("gatewayClient") WebClient.Builder webClientBuilder,
                                      @Value("${urls.gateway}") String gatewayUrl) {
        System.out.println(gatewayUrl);
        this.islandResourceWebClient = webClientBuilder.baseUrl(gatewayUrl).build();
    }

    public Mono<IslandResourceDTO> assignResources(String islandId, RawMaterialsAndPopulationCost resourceAllocationRequestDTO) {
        return islandResourceWebClient.post()
                .uri("/resource/private/assignResources/{islandId}", islandId)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(resourceAllocationRequestDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(Mono::error);
    }

    public Mono<Long> whoami(String jwtToken) {
        return islandResourceWebClient.get()
                .uri("/auth/me")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnError(Mono::error);
    }
}
