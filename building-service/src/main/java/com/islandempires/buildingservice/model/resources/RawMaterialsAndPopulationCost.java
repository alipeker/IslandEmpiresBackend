package com.islandempires.buildingservice.model.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialsAndPopulationCost {

    private Integer wood;

    private Integer clay;

    private Integer iron;

    private Integer population;

}
