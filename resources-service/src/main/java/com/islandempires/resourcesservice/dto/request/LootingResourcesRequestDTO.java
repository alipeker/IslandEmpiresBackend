package com.islandempires.resourcesservice.dto.request;

import com.islandempires.resourcesservice.dto.PopulationDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LootingResourcesRequestDTO implements Serializable {
    @NotNull
    private Integer lootingSize;

    @NotNull
    private PopulationDTO populationDTO;
}
