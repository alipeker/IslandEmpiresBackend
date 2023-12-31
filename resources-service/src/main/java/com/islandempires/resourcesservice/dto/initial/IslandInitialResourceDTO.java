package com.islandempires.resourcesservice.dto.initial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandInitialResourceDTO {
    private String islandId;

    private Integer wood;
    private Integer iron;
    private Integer clay;
    private Integer gold;

    private Integer rawMaterialStorageSize;

    private Integer hourlyWoodProduction;
    private Integer hourlyIronProduction;
    private Integer hourlyClayProduction;
    private Integer hourlyGoldProduction;

    private Integer meat;
    private Integer fish;
    private Integer wheat;

    private Integer foodStorageSize;

    private Integer population;
    private Integer hourlyPopulationGrowth;
    private Integer populationLimit;
}
