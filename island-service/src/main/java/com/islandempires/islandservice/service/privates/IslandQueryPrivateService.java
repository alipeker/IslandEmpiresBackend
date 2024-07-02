package com.islandempires.islandservice.service.privates;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.model.Island;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandQueryPrivateService {
    Mono<IslandDTO> get(String islandId);

    Flux<Island> getAll();

    Mono<Boolean> isUserIslandOwner(String islandId, Long userid);

    Mono<Double> calculateDistance(String islandId1, String islandId2);
}