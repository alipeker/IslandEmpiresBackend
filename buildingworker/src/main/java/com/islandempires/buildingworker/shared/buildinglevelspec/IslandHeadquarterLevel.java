package com.islandempires.buildingworker.shared.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandHeadquarterLevel extends BuildingLevel {

    private double timeReductionPercentage;

}