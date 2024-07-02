package com.islandempires.resourcesservice.service.privates;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import reactor.core.publisher.Mono;

public interface IslandResourceModificationPrivateService {
    Mono<IslandResourceDTO> updateIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number newValue);

    Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number value);

    Mono<Void> delete(String islandId);
}