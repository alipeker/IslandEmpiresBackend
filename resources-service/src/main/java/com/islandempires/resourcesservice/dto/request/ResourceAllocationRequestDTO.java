package com.islandempires.resourcesservice.dto.request;

import com.islandempires.resourcesservice.dto.PopulationDTO;
import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceAllocationRequestDTO implements Serializable {
    @NotNull
    private RawMaterialsDTO rawMaterials;

    @NotNull
    private PopulationDTO population;

}
