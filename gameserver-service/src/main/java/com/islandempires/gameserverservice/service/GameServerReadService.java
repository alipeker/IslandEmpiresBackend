package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.enums.IslandBuildingEnum;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.GameServerSoldier;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameServerReadService {
    GameServer getGameServerInfo(String islandId);

    InitialGameServerPropertiesDTO getGameServerInitialProperties(String serverId);
    Flux<AllBuildings> getGameServerBuildingProperties();
    Flux<GameServerSoldier> getGameServerSoldierProperties();

    GameServer getServerBuildingInfo(String serverId);

    Mono<GameServer> testali(String serverId);
}
