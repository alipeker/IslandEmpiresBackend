package com.islandempires.resourcesservice.service.privates;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface IslandResourceModificationPrivateService {
    Mono<IslandResourceDTO> updateIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number newValue);

    Mono<IslandResourceDTO> increaseOrDecreaseIslandResourceField(String islandId, IslandResourceEnum islandResourceEnum, Number value);

    Mono<IslandResourceDTO> updateIslandTrading(String islandId, int totalShipNumber, long shipCapacity, int timeReductionPercentage);

    Mono<Void> delete(String islandId);
}