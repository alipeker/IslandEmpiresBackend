package com.islandempires.resourcesservice.controller;

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

    @PostMapping("/")
    public Mono<IslandResource> save(@RequestBody IslandResource islandResource) {
        return this.islandResourceService.save(islandResource);
    }

    @PutMapping("/")
    public Mono<IslandResource> update(@RequestBody IslandResource islandResource) {
        return this.islandResourceService.update(islandResource);
    }


}
