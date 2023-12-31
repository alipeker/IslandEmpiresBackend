package com.islandempires.resourcesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RawMaterialsDTO {
    private Integer wood;
    private Integer iron;
    private Integer clay;
    private Integer gold;

    private Integer rawMaterialStorageSize;
}
