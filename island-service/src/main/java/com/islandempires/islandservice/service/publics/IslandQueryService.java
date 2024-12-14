package com.islandempires.islandservice.service.publics;

import com.islandempires.islandservice.dto.IslandDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandQueryService {
    Mono<IslandDTO> get(String islandId, Long userid);
    Flux<IslandDTO> getAllUserIslands(String serverId, Long userid);
    Mono<Boolean> isUserIslandOwner(String islandId, Long userid);
    Mono<Double> calculateDistance(String islandId1, String islandId2);
}