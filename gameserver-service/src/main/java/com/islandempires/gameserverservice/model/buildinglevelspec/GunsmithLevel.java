package com.islandempires.gameserverservice.model.buildinglevelspec;

import com.islandempires.gameserverservice.model.requirement.SoldierRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GunsmithLevel extends BuildingLevel {

    private int timeReductionPercentage;

    private List<SoldierRequirement> soldierRequirements;

}
