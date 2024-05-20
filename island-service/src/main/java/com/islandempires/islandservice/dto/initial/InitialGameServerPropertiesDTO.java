package com.islandempires.islandservice.dto.initial;

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
    private InitialAllBuildingsDTO initialAllBuildings;
    private InitialIslandResourceDTO islandResource;

}
