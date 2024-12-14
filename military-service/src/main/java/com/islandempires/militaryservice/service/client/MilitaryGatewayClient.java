package com.islandempires.militaryservice.service.client;

import com.islandempires.militaryservice.dto.GameServerSoldierDTO;
import com.islandempires.militaryservice.dto.IslandResourceDTO;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.production.SoldierProduction;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "militaryGatewayClient", url = "${urls.gateway}", configuration = FeignClientConfig.class)
public interface MilitaryGatewayClient {

    @GetMapping("/gameservice/private/getGameServerSoldierProperties")
    List<GameServerSoldierDTO> getGameServerSoldierProperties();

    @GetMapping("/island/private/calculateDistance/{islandId1}/{islandId2}")
    Double getDistanceBetweenIslands(@PathVariable String islandId1, @PathVariable String islandId2);

    @PostMapping("/resource/private/assignResources/{islandId}")
    IslandResourceDTO assignResources(@PathVariable String islandId, @RequestBody RawMaterialsAndPopulationCost resourceAllocationRequestDTO);

    @PostMapping("/resource/private/refundResources/{islandId}")
    IslandResourceDTO refundResources(@PathVariable String islandId, @RequestBody RawMaterialsAndPopulationCost resourceAllocationRequestDTO);

    @PostMapping("/private/websocket/military/{serverId}/{userId}")
    void islandMilitaryChange(@PathVariable String serverId, @PathVariable Long userId, @RequestBody IslandMilitary islandMilitary);
}
