package com.islandempires.gameserverservice.model.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialsAndPopulationCost implements Serializable {

    private Integer wood;

    private Integer clay;

    private Integer iron;

    private Integer population;

}
