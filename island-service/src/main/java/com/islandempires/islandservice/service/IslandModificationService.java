package com.islandempires.islandservice.service;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

public interface IslandModificationService {
    Mono<IslandDTO> create(Long userId, InitialGameServerPropertiesDTO initialGameServerPropertiesDTO, String jwtToken, String serverId);

    Mono<IslandDTO> updateName(Long userId, String islandID, String newName);

    Mono<IslandDTO> updateOwner(Long userId, String islandId);

    Mono<Void> delete(String islandId);
}