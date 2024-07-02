package com.islandempires.resourcesservice.service.privates;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IslandResourceQueryPrivateService {
    Flux<IslandResourceDTO> getAll();
    Mono<IslandResourceDTO> get(String islandId);
}
