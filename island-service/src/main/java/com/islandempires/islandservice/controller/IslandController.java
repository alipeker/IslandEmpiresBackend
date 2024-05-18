package com.islandempires.islandservice.controller;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.UpdateIslandDTO;
import com.islandempires.islandservice.dto.UpdateOwnerDTO;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.service.IslandModificationService;
import com.islandempires.islandservice.service.IslandQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@CrossOrigin
@RestController
@RequestMapping("/island")
public class IslandController {
    @Autowired
    private IslandQueryService islandQueryService;

    @Autowired
    private IslandModificationService islandModificationService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("test")
    public Mono<String> test() {
        return Mono.just("a");
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
    @GetMapping("/getAll")
    public Flux<Island> getAll() {
        return islandQueryService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getIsland/{islandid}")
    public Mono<IslandDTO> getIsland(@PathVariable String islandid, @RequestAttribute("userId") Long userid) {
        return islandQueryService.getIsland(islandid, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Mono<IslandDTO> create(@RequestAttribute("userId") Long userid) {
        return islandModificationService.create(userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateName")
    public Mono<IslandDTO> updateName(@RequestBody UpdateIslandDTO updateIslandDTO, @RequestAttribute("userId") Long userid) {
        return islandModificationService.updateName(userid, updateIslandDTO.getIslandID(), updateIslandDTO.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/updateOwner")
    public Mono<IslandDTO> updateOwner(@RequestBody UpdateOwnerDTO updateOwnerDTO, @RequestAttribute("userId") Long userid) {
        return islandModificationService.updateOwner(updateOwnerDTO.getUserId(), updateOwnerDTO.getIslandID());
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{islandId}")
    public Mono<Void> delete(@RequestParam String islandId) {
        return islandModificationService.delete(islandId);
    }

}
