package com.islandempires.buildingservice.service.client;

import com.islandempires.buildingservice.dto.*;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.islandempires.buildingservice.enums.SoldierSubTypeEnum;

@Service
public class GatewayWebClientService {

    private final WebClient webClient;

    @Autowired
    public GatewayWebClientService(WebClient.Builder webClientBuilder,
                                   @Value("${urls.gateway}") String gatewayUrl) {
        this.webClient = webClientBuilder
                .baseUrl(gatewayUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<IslandResourceDTO> assignResources(String islandId, RawMaterialsAndPopulationCost resourceAllocationRequestDTO) {
        return webClient.post()
                .uri("/resource/private/assignResources/" + islandId)
                .bodyValue(resourceAllocationRequestDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(Mono::error);
    }

    public Mono<IslandResourceDTO> refundResources(String islandId, RawMaterialsAndPopulationCost resourceAllocationRequestDTO) {
        return webClient.post()
                .uri("/resource/private/refundResources/" + islandId)
                .bodyValue(resourceAllocationRequestDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(Mono::error);
    }

    public Mono<Long> whoami(String jwtToken) {
        return webClient.get()
                .uri("/auth/me")
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnError(Mono::error);
    }

    public Mono<IslandResourceDTO> updateIslandResourceField(String islandId, UpdateIslandResourceFieldDTO updateIslandResourceFieldDTO) {
        return webClient.patch()
                .uri("/resource/private/updateIslandResourceField/{islandId}", islandId)
                .bodyValue(updateIslandResourceFieldDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(Mono::error);
    }

    public Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(String islandId, IncreaseOrDecreaseIslandResourceFieldDTO increaseOrDecreaseIslandResourceFieldDTO) {
        return webClient.patch()
                .uri("/resource/private/increaseOrDecreaseIslandResourceField/{islandId}", islandId)
                .bodyValue(increaseOrDecreaseIslandResourceFieldDTO)
                .retrieve()
                .bodyToMono(IslandResourceDTO.class)
                .doOnError(Mono::error);
    }

    public Mono<Void> sendWebsocket(String serverId, Long userId, String islandId, IslandBuildingAndScheduledJobDTO islandBuilding) {
        return webClient.post()
                .uri("/private/websocket/" + serverId + "/" + userId + "/" + islandId)
                .bodyValue(islandBuilding)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> joinClanAllowed(String serverId, Long userId) {
        return webClient.patch()
                .uri("/clan/private/joinClanAllowed/" + serverId + "/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(Mono::error);
    }

    public Mono<Void> createClanAllowed(String serverId, Long userId, int maxMemberNumber) {
        return webClient.patch()
                .uri("/clan/private/createClanAllowed/" + serverId + "/" + userId)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(Mono::error);
    }

    public Mono<Void> setClanMaxMemberNumber(String serverId, Long userId, int maxMemberNumber) {
        return webClient.patch()
                .uri("/clan/private/setClanMaxMemberNumber/" + serverId + "/" + userId + "/" + maxMemberNumber)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(Mono::error);
    }

    public Mono<Void> updateInfantrymanTimeReduction(String islandId, int percentage) {
        return webClient.put()
                .uri("/military/private/infantrymanTimeReduction/" + islandId + "/" + percentage)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(Mono::error);
    }

    public Mono<Void> updateRifleTimeReduction(String islandId, int percentage) {
        return webClient.put()
                .uri("/military/private/rifleTimeReduction/" + islandId + "/" + percentage)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateCannonTimeReduction(String islandId, int percentage) {
        return webClient.put()
                .uri("/military/private/cannonTimeReduction/" + islandId + "/" + percentage)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateShipTimeReduction(String islandId, int percentage) {
        return webClient.put()
                .uri("/military/private/shipTimeReduction/" + islandId + "/" + percentage)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateDefencePointPercentage(String islandId, double percentage) {
        return webClient.put()
                .uri("/military/private/defencePointPercentage/" + islandId + "/" + percentage)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateVisibilityRangeAndObservableCapacity(String islandId, int range, int capacity) {
        return webClient.put()
                .uri("/military/private/visibilityRangeAndObservableCapacity/" + islandId + "/" + range + "/" + capacity)
                .retrieve()
                .bodyToMono(Void.class);
    }


    public Mono<Void> updateMaxMissionaryCount(String islandId, int maxCount) {
        return webClient.put()
                .uri("/military/private/maxMissionaryCount/" + islandId + "/" + maxCount)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> setSoldierTypeResearched(String islandId, SoldierSubTypeEnum soldierSubTypeEnum, boolean researched) {
        return webClient.put()
                .uri("/military/private/setSoldierTypeResearched/" + islandId + "/" + soldierSubTypeEnum + "/" + researched)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateIslandTrading(String islandId, UpdateIslandTradingDTO updateIslandTradingDTO) {
        return webClient.put()
                .uri("/resource/private/updateIslandTrading/" + islandId )
                .bodyValue(updateIslandTradingDTO)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

