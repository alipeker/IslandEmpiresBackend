package com.islandempires.gameserverservice.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MillLevel extends BuildingLevel {

    private int hourlyWheatProduction;

}