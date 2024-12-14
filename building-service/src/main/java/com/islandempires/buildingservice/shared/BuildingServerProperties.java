package com.islandempires.buildingservice.shared;

import com.islandempires.buildingservice.shared.building.AllBuildings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document("BuildingServerProperties")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingServerProperties implements Serializable {
    private String serverId;

    private AllBuildings allBuildings;

    private LocalDateTime timestamp = LocalDateTime.now();

}
