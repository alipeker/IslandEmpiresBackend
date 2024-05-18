package com.islandempires.buildingworker.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DairyFarmLevel extends BuildingLevel {

    private Integer hourlyMeatProduction;


}