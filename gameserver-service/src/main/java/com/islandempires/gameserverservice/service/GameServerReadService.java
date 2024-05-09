package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.model.GameServerIslands;
import reactor.core.publisher.Mono;

public interface GameServerReadService {
    Mono<GameServerIslands> getGameServerInfo(String islandId);
}
