package com.islandempires.islandservice.service;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.model.Island;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandQueryService {
    Mono<IslandDTO> get(String islandId, Long userid);

    Flux<Island> getAll();

    Mono<IslandDTO> getIsland(String islandId, Long userid);

    Mono<Boolean> isUserIslandOwner(String islandId, Long userid);
}