package com.islandempires.islandservice.service.publics;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.model.Island;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandQueryService {
    Mono<IslandDTO> get(String islandId, Long userid);

    Mono<Boolean> isUserIslandOwner(String islandId, Long userid);
    Mono<Double> calculateDistance(String islandId1, String islandId2);
}