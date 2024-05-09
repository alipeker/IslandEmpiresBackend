package com.islandempires.islandservice.service;

import com.islandempires.islandservice.dto.IslandDTO;
import reactor.core.publisher.Mono;

public interface IslandQueryService {
    Mono<IslandDTO> get(String islandId, Long userid);


}