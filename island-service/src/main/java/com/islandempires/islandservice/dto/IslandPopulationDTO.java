package com.islandempires.islandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IslandPopulationDTO implements Serializable {

    private String islandId;

    private Long population;

}
