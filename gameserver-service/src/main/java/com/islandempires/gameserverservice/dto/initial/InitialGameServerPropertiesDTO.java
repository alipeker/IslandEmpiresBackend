package com.islandempires.gameserverservice.dto.initial;

import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.dto.request.IslandCreateRequestDTO;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class InitialGameServerPropertiesDTO implements Serializable {
    private static final long serialVersionUID = 2562090437133615330L;

    @NonNull
    private InitialAllBuildingsDTO initialAllBuildings;

    @NonNull
    private IslandResourceDTO islandResource;

    private IslandCreateRequestDTO islandCreateRequestDTO;

}
