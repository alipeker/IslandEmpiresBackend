package com.islandempires.resourcesservice.controller;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.returndto.ResourceOfferDTO;
import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.*;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceModificationService;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceQueryService;
import com.islandempires.resourcesservice.service.privates.IslandResourceInteractionPrivateService;
import com.islandempires.resourcesservice.service.privates.IslandResourceModificationPrivateService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("resource/public")
public class IslandResourceController {

    @Autowired
    private IslandResourceQueryService islandResourceQueryService;

    @Autowired
    private IslandResourceModificationService islandResourceModificationService;

    @Autowired
    private WhoAmIClient whoAmIClient;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandResourceDTO> get(@PathVariable String islandid, @RequestAttribute("userId") Long userId) {
        return islandResourceQueryService.get(islandid, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/checkResourceAllocation/{islandId}")
    public Mono<Boolean> checkResourceAllocation(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                 @Valid @RequestBody ResourceAllocationRequestDTO resourceAllocationRequestDTO,
                                                 @RequestAttribute("userId") Long userId) {
        return islandResourceQueryService.validateResourceAllocationForIsland(islandId, resourceAllocationRequestDTO, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/transportResources/{senderIslandId}/{receiverIslandId}")
    public Mono<IslandResourceDTO> transportResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String senderIslandId,
                                            @Size(min = 1, message = "Id must be not empty") @PathVariable String receiverIslandId,
                                                 @Valid @RequestBody RawMaterialsDTO rawMaterialsDTO,
                                                 @RequestAttribute("userId") Long userId) {
        return islandResourceModificationService.transportResources(senderIslandId, receiverIslandId, rawMaterialsDTO, userId);
    }


    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/cancelTransportResources/{transportId}")
    public Mono<Void> cancelTransportResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String transportId,
                                                      @RequestAttribute("userId") Long userId) {
        return islandResourceModificationService.cancelTransportResources(transportId, userId);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/createOffer/{islandId}")
    public Mono<IslandResourceDTO> createOffer(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                                      @Valid @RequestBody BidderAcceptorRawMaterialsDTO bidderAcceptorRawMaterialsDTO,
                                                      @RequestAttribute("userId") Long userId) {
        return islandResourceModificationService.createOffer(islandId, bidderAcceptorRawMaterialsDTO.getBidderRawMaterials(), bidderAcceptorRawMaterialsDTO.getAcceptorRawMaterials(), userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/acceptOffer/{offerId}/{islandId}")
    public Mono<IslandResourceDTO> acceptOffer(@Size(min = 1, message = "Id must be not empty") @PathVariable String offerId,
                                                @Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                               @RequestAttribute("userId") Long userId) {
        return islandResourceModificationService.acceptOffer(offerId, islandId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{serverId}/{pageNumber}/{pageSize}")
    public Flux<ResourceOfferDTO> getAllResourceOffer(@PathVariable String serverId,
                                                      @PathVariable int pageNumber,
                                                      @PathVariable int pageSize) {
        return islandResourceQueryService.getAllResourceOffer(serverId, pageNumber, pageSize);
    }
}
