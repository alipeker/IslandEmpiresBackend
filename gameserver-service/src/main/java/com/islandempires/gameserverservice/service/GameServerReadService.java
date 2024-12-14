package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.response.GameServerIdDTO;
import com.islandempires.gameserverservice.dto.response.GameServerSoldierDTO;
import com.islandempires.gameserverservice.dto.response.ServerUserRegistrationInfoDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerAllBuildings;
import com.islandempires.gameserverservice.model.GameServerSoldier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameServerReadService {
    GameServer getGameServerInfo(String islandId);

    Mono<GameServer> getGameServerInitialProperties(String serverId);
    Flux<GameServerAllBuildings> getGameServerBuildingProperties();
    Flux<GameServerSoldierDTO> getGameServerSoldierProperties();
    Flux<GameServerIdDTO> getGameServers();

    Mono<GameServerSoldier> getServerBuildingInfo(String serverId);

    Mono<GameServer> testali(String serverId);

    Flux<ServerUserRegistrationInfoDTO> getUserServers(Long userId);
}
