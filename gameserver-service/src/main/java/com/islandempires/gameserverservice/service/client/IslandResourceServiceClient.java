package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@FeignClient(value = "island-resource", url = "${urls.island-resource}")
@Component
public interface IslandResourceServiceClient {
    @PostMapping(value = "/resource/")
    IslandResourceDTO initializeIslandResource(@RequestHeader("userid") Long userid, @RequestBody IslandResourceDTO initialIslandResourceDTO);

}
