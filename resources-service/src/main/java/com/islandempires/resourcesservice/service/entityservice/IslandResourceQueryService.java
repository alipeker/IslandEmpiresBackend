package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandResourceQueryService {
    Flux<IslandResourceDTO> getAll();
    Mono<IslandResourceDTO> get(String islandId, Long userId);
}
