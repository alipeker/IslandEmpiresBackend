package com.islandempires.resourcesservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RawMaterialsProductionDTO {
    private Integer hourlyWoodProduction;
    private Integer hourlyIronProduction;
    private Integer hourlyClayProduction;
    private Integer hourlyGoldProduction;
}
