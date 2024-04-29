package com.islandempires.gameserverservice.service;

import com.islandempires.gameserverservice.model.Island;
import reactor.core.publisher.Mono;

public interface GameServerReadService {
    Mono<Island> getGameServerInfo(String islandId);
}
