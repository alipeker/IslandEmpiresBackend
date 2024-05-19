package com.islandempires.islandservice.dto.initial;

import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InitialGameServerPropertiesDTO implements Serializable {
    private static final long serialVersionUID = 2562090437133615330L;

    private InitialAllBuildingsDTO initialAllBuildings;
    private IslandResourceDTO islandResource;

}
