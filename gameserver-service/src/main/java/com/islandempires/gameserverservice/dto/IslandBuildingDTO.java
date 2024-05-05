package com.islandempires.gameserverservice.dto;

import com.islandempires.gameserverservice.model.buildingtype.BaseStructures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandBuildingDTO {
    private String id;

    private String islandId;

}
