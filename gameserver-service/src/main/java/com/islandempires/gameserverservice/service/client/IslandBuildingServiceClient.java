package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "island-building", url = "${urls.island-building}")
@Component
public interface IslandBuildingServiceClient {
    @PostMapping(value = "/building/{islandId}")
    IslandBuildingDTO initializeIslandBuildings(@PathVariable String islandId, @RequestBody AllBuildings allBuildingList);

}
