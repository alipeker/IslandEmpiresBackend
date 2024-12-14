package com.islandempires.islandservice.controller;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.UpdateOwnerDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.service.privates.IslandModificationPrivateService;
import com.islandempires.islandservice.service.privates.IslandQueryPrivateService;
import com.islandempires.islandservice.service.publics.IslandModificationService;
import com.islandempires.islandservice.service.publics.IslandQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin
@RestController
@RequestMapping("/island/private")
public class IslandPrivateController {
    @Autowired
    private IslandQueryPrivateService islandQueryService;

    @Autowired
    private IslandQueryService islandQueryServicePublic;

    @Autowired
    private IslandModificationPrivateService islandModificationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandDTO> get(@PathVariable String islandid) {
        return islandQueryService.get(islandid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllUserIslands/{serverId}/{userId}")
    public Flux<IslandDTO> getAllUserIslands(@PathVariable String serverId, @PathVariable Long userId) {
        return islandQueryServicePublic.getAllUserIslands(serverId, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAll")
    public Flux<Island> getAll() {
        return islandQueryService.getAll();
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{serverId}/{userId}")
    public Mono<IslandDTO> create(@RequestBody InitialGameServerPropertiesDTO initialGameServerPropertiesDTO,
                                  @PathVariable String serverId, @PathVariable Long userId) {
        return islandModificationService.create(userId, initialGameServerPropertiesDTO, serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateOwner")
    public Mono<IslandDTO> updateOwner(@RequestBody UpdateOwnerDTO updateOwnerDTO) {
        return islandModificationService.updateOwner(updateOwnerDTO.getUserId(), updateOwnerDTO.getIslandID());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{islandId}")
    public Mono<Void> delete(@RequestParam String islandId) {
        return islandModificationService.delete(islandId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/calculateDistance/{islandId1}/{islandId2}")
    public Mono<Double> calculateDistance(@PathVariable String islandId1, @PathVariable String islandId2) {
        return islandQueryService.calculateDistance(islandId1, islandId2);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/isUserIslandOwner/{islandId}")
    public Mono<Boolean> isUserIslandOwner(@PathVariable String islandId, @RequestAttribute("userId") Long userid) {
        return islandQueryService.isUserIslandOwner(islandId, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/run")
    public Flux<Island> run() {
        return islandModificationService.run();
    }
}

