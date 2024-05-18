package com.islandempires.buildingworker.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BricksWorksLevel extends BuildingLevel {
    private Integer clayProductionIncreasePercentage;
}
