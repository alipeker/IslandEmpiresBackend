package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.request.IslandCreateRequestDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import reactor.core.publisher.Mono;

public interface GameServerWriteService {

    Mono<Void> initializeGameServerProperties(GameServerDTO gameServerDTO);

    Mono<GameServerIslands> initializeIsland(IslandCreateRequestDTO islandCreateRequestDTO, String serverId, Long userId);
}
