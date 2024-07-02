package com.islandempires.islandservice.service.publics;

import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

public interface IslandModificationService {
    Mono<IslandDTO> updateName(Long userId, String islandID, String newName);
}