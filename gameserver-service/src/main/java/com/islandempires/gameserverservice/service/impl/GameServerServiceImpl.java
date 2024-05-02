package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.IslandDTO;
import com.islandempires.gameserverservice.dto.IslandResourceDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.Island;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import com.islandempires.gameserverservice.service.client.IslandResourceServiceClient;
import com.islandempires.gameserverservice.service.client.IslandServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameServerServiceImpl implements GameServerWriteService, GameServerReadService {

    @Autowired
    private GameServerRepository gameServerRepository;

    @Autowired
    private GameServerIslandsRepository gameServerIslandsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IslandServiceClient islandServiceClient;

    @Autowired
    private IslandResourceServiceClient islandResourceServiceClient;


    @Override
    public Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerRepository.save(modelMapper.map(gameServerDTO, GameServer.class));
    }

    @Override
    public Mono<IslandDTO> initializeIsland(String serverId, Long userId) {
        /**
         * Firstly send island initial info to Island Service and create island.
         * Then add sisland to server
         * finally return
         * "Outbox transaction pattern"
         */

        IslandDTO islandDTO = islandServiceClient.create(userId);

        try {
            IslandResourceDTO islandResourceDTO = new IslandResourceDTO();
            islandResourceDTO.setIslandId(islandDTO.getId());
            islandResourceServiceClient.initializeIslandResource(userId, islandResourceDTO);
        } catch (Exception e) {
            islandServiceClient.rollBackIslandCreate(islandDTO.getId());
        }

        return null;

        /*return gameServerIslandsRepository.save(new Island(islandId, userId));*/
    }

    @Override
    public Mono<Island> getGameServerInfo(String islandId) {
        return null;
    }
}
