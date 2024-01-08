package com.islandempires.resourcesservice.dto;

import com.islandempires.resourcesservice.enums.RawMaterialEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RawMaterialDTO {
    private RawMaterialEnum rawMaterialEnum;

    private Integer numberOfMaterial;
}
