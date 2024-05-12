package com.islandempires.buildingservice.model;

import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("IslandBuilding")
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
