package com.islandempires.buildingworker.model;

import com.islandempires.buildingworker.shared.buildingtype.BaseStructures;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandBuilding implements Serializable {
    @Id
    private String id;

    private Long userId;

    private List<BaseStructures> allBuildingList;

    public void setAllBuildings(List<BaseStructures> allBuildings) {
        this.allBuildingList = allBuildings;
    }
}
