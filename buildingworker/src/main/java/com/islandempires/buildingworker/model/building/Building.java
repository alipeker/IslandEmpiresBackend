package com.islandempires.buildingworker.model.building;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Building implements Serializable {
    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private int lvl = 0;
}
