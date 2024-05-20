package com.islandempires.buildingworker.dto;

import com.islandempires.buildingworker.enums.IslandResourceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncreaseOrDecreaseIslandResourceFieldDTO implements Serializable {
    private IslandResourceEnum islandResourceEnum;
    private Number value;
}

