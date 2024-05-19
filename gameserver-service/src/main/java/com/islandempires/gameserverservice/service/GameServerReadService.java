package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import reactor.core.publisher.Mono;

public interface GameServerReadService {
    GameServer getGameServerInfo(String islandId);

    InitialGameServerPropertiesDTO getGameServerInitialProperties(String serverId);

    Mono<GameServer> testali(String serverId);
}
