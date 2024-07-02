package com.islandempires.resourcesservice.controller;

import com.islandempires.resourcesservice.filter.client.WhoAmIClient;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.*;
import com.islandempires.resourcesservice.service.entityservice.IslandResourceQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("resource/public")
public class IslandResourceController {

    @Autowired
    private IslandResourceQueryService islandResourceQueryService;

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

}
