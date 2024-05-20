package com.islandempires.buildingservice.model;

import com.islandempires.buildingservice.model.building.AllBuildings;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("IslandBuilding")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandBuilding implements Serializable {
    @Id
    private String id;

    private Long userId;

    private String serverId;

    private AllBuildings allBuilding;

}
