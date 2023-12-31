package com.islandempires.resourcesservice.dto.request;

import com.islandempires.resourcesservice.dto.PopulationDTO;
import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IsResourcesEnoughControl {
    private RawMaterialsDTO rawMaterialsDTO;
    private PopulationDTO populationDTO;
}
