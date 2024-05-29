package com.islandempires.militaryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportMilitaryUnits {

    private String ownerIslandId;

    private IslandMilitaryUnits islandMilitaryUnits;

}
