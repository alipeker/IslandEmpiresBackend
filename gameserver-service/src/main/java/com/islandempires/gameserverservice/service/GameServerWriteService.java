package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import reactor.core.publisher.Mono;

public interface GameServerWriteService {

    Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO);

    Mono<IslandDTO> initializeIsland(String serverId, Long userId);
}
