package com.islandempires.resourcesservice.controller;

import com.islandempires.resourcesservice.dto.initial.IslandInitialResourceDTO;
import com.islandempires.resourcesservice.dto.request.IsResourcesEnoughControl;
import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.service.IslandResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/resource")
public class IslandResourceController {

    @Autowired
    private IslandResourceService islandResourceService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<IslandResource> streamResources() {
        return this.islandResourceService.getAll();
    }

    @GetMapping("/{islandid}")
    public Mono<IslandResource> get(@PathVariable String islandid) {
        return this.islandResourceService.get(islandid);
    }

    @GetMapping("/isResourcesEnoughControl/{islandid}")
    public Mono<Boolean> isResourcesEnoughControl(@PathVariable String islandid, @RequestBody IsResourcesEnoughControl isResourcesEnoughControl) {
        return islandResourceService.isResourcesEnoughControl(islandid, isResourcesEnoughControl);
    }

    @PostMapping("/")
    public Mono<IslandResource> prepareIslandResource(@RequestBody IslandInitialResourceDTO initialIslandResourceDTO) {
        return islandResourceService.prepareIslandInitialResource(initialIslandResourceDTO);
    }

    @PutMapping("/")
    public Mono<IslandResource> update(@RequestBody IslandResource islandResource) {
        return islandResourceService.updateIslandResource(islandResource);
    }

    @PostMapping("/test")
    public Flux<IslandResource> addTest() {
        return islandResourceService.addTest();
    }

}
