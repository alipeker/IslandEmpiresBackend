package com.islandempires.resourcesservice.dto.returndto;

import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LootingResourcesReturnDTO implements Serializable {
    @NotNull
    private RawMaterialsDTO rawMaterialsDTO;
}
