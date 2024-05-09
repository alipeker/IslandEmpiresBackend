package com.islandempires.gameserverservice.dto;

import com.islandempires.gameserverservice.dto.island.IslandDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitializeIslandResponseDTO {
    private IslandDTO islandDTO;
    private GameServerDTO gameServerDTO;
}
