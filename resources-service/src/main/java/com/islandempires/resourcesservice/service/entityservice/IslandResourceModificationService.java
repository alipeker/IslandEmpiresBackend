package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.enums.IslandResourceEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface IslandResourceModificationService {

    Mono<IslandResourceDTO> transportResources(String senderIslandId, String receiverIslandId, RawMaterialsDTO rawMaterialsDTO, Long userId);

    Mono<IslandResourceDTO> createOffer(String islandId, RawMaterialsDTO offeringRawMaterialsDTO, RawMaterialsDTO wantedRawMaterials, Long userId);

    Mono<Void> cancelTransportResources(String transportId, Long userId);

    Mono<IslandResourceDTO> acceptOffer(String offerId, String islandId, Long userId);
}