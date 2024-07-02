package com.islandempires.mapservice.client;

import com.islandempires.mapservice.dto.IslandDTO;
import com.islandempires.mapservice.dto.IslandResourceDTO;
import com.islandempires.mapservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class GatewayWebClient {

    private final WebClient islandResourceWebClient;

    @Autowired
    public GatewayWebClient(@Qualifier("gatewayClient") WebClient.Builder webClientBuilder,
                        @Value("${urls.gateway}") String gatewayUrl) {
        this.islandResourceWebClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Flux<IslandDTO> getAllIsland() {
        return islandResourceWebClient.get()
                .uri("/island/private/getAll")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(IslandDTO.class)
                .doOnError(Flux::error);
    }

    public Flux<IslandResourceDTO> getAllIslandResource() {
        return islandResourceWebClient.get()
                .uri("/resource/private/getAll")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(IslandResourceDTO.class)
                .doOnError(Flux::error);
    }

    public Flux<UserDTO> getAllUsers() {
        return islandResourceWebClient.get()
                .uri("/auth/private/getAllUsers")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(UserDTO.class)
                .doOnError(Flux::error);
    }
}
