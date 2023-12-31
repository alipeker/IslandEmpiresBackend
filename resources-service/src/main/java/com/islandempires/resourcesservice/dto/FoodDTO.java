package com.islandempires.resourcesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodDTO {
    private Integer meat;
    private Integer fish;
    private Integer wheat;
}
