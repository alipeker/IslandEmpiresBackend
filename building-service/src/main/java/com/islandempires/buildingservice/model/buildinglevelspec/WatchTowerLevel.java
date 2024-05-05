package com.islandempires.buildingservice.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchTowerLevel extends BuildingLevel {

    private Double visibilityRange;

    private Integer observableCapacity;
}
