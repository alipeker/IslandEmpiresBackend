package com.islandempires.buildingservice.shared.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchTowerLevel extends BuildingLevel {

    private int visibilityRange;

    private BigInteger observableCapacity;
}
