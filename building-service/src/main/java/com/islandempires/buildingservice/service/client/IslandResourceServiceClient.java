package com.islandempires.buildingservice.service.client;

import com.islandempires.buildingservice.model.resources.RawMaterialsAndPopulationCost;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "island-resource", url = "${urls.island-resource}")
@Component
public interface IslandResourceServiceClient {
    @PatchMapping(value = "/resource/assignResources/{islandId}")
    Object assignResources(@Size(min = 1, message = "Id must be not empty") @PathVariable String islandId,
                                               @Valid @RequestBody RawMaterialsAndPopulationCost resourceAllocationRequestDTO);


}
