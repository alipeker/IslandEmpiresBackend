package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.IslandDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.Island;
import reactor.core.publisher.Mono;

public interface GameServerWriteService {

    Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO);

    Mono<IslandDTO> initializeIsland(String serverId, Long userId);
}
