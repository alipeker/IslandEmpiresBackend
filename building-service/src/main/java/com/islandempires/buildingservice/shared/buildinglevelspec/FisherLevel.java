package com.islandempires.buildingservice.shared.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FisherLevel extends BuildingLevel {

    private int hourlyFishProduction;

}