package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.IslandDTO;
import com.islandempires.gameserverservice.dto.IslandResourceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "island-resource", url = "${urls.island-resource}")
public interface IslandResourceServiceClient {
    @PostMapping(value = "/resource/initializeIslandResource/{userid}")
    IslandResourceDTO initializeIslandResource(@RequestHeader("userid") Long userid, @RequestBody IslandResourceDTO initialIslandResourceDTO);

}
