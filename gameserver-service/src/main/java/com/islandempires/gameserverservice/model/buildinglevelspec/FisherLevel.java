package com.islandempires.gameserverservice.model.buildinglevelspec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FisherLevel extends BuildingLevel {

    private int hourlyFishProduction;

}