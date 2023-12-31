package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.dto.initial.IslandInitialResourceDTO;
import com.islandempires.resourcesservice.dto.request.IsResourcesEnoughControl;
import com.islandempires.resourcesservice.model.IslandResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandResourceService {
    Flux<IslandResource> getAll();

    Mono<IslandResource> get(String islandId);

    Mono<Boolean> isResourcesEnoughControl(String islandId, IsResourcesEnoughControl isResourcesEnoughControl);

    Mono<IslandResource> prepareIslandInitialResource(IslandInitialResourceDTO initialIslandResorceDTO);

    Mono<IslandResource> updateIslandResource(IslandResource islandResource);


    Flux<IslandResource> addTest();
}
