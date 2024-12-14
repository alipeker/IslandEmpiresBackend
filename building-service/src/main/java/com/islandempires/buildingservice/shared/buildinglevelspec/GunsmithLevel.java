package com.islandempires.buildingservice.shared.buildinglevelspec;

import com.islandempires.buildingservice.shared.requirement.SoldierRequirement;
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