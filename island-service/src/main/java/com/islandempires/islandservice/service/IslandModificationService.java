package com.islandempires.islandservice.service;

import com.islandempires.islandservice.dto.IslandDTO;
import reactor.core.publisher.Mono;

public interface IslandModificationService {
    Mono<IslandDTO> create(Long userId);

    Mono<IslandDTO> updateName(Long userId, String islandID, String newName);

    Mono<IslandDTO> updateOwner(Long userId, String islandId);

    Mono<Boolean> delete(String islandId);
}