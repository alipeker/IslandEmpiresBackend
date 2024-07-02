package com.islandempires.islandservice.service.privates;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import reactor.core.publisher.Mono;

public interface IslandModificationPrivateService {
    Mono<IslandDTO> create(Long userId, InitialGameServerPropertiesDTO initialGameServerPropertiesDTO, String serverId);

    Mono<IslandDTO> updateOwner(Long userId, String islandId);

    Mono<Void> delete(String islandId);
}