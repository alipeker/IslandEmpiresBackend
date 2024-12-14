package com.islandempires.islandservice.controller;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.UpdateIslandDTO;
import com.islandempires.islandservice.service.publics.IslandModificationService;
import com.islandempires.islandservice.service.publics.IslandQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/island/public")
public class IslandController {
    @Autowired
    private IslandQueryService islandQueryService;

    @Autowired
    private IslandModificationService islandModificationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("calculateDistance/{islandId1}/{islandId2}")
    public Mono<Double> calculateDistance(@PathVariable String islandId1, @PathVariable String islandId2) {
        return islandQueryService.calculateDistance(islandId1, islandId2);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/isUserIslandOwner/{islandid}")
    public Mono<Boolean> isUserIslandOwner(@PathVariable String islandid, @RequestAttribute("userId") Long userid) {
        return islandQueryService.isUserIslandOwner(islandid, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandid}")
    public Mono<IslandDTO> get(@PathVariable String islandid, @RequestAttribute("userId") Long userid) {
        return islandQueryService.get(islandid, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getAllUserIslands/{serverId}")
    public Flux<IslandDTO> getAllUserIslands(@PathVariable String serverId, @RequestAttribute("userId") Long userid) {
        return islandQueryService.getAllUserIslands(serverId, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateName")
    public Mono<IslandDTO> updateName(@RequestBody UpdateIslandDTO updateIslandDTO, @RequestAttribute("userId") Long userid) {
        return islandModificationService.updateName(userid, updateIslandDTO.getIslandID(), updateIslandDTO.getName());
    }

}
