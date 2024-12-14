package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.dto.returndto.ResourceOfferDTO;
import com.islandempires.resourcesservice.model.IslandResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandResourceQueryService {
    Mono<IslandResourceDTO> get(String islandId, Long userId);

    Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO, Long userId);

    Flux<ResourceOfferDTO> getAllResourceOffer(String serverId, int pageNumber, int pageSize);
}
