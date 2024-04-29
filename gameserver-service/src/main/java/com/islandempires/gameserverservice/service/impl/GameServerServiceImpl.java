package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.Island;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
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


    @Override
    public Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerRepository.save(modelMapper.map(gameServerDTO, GameServer.class));
    }

    @Override
    public Mono<Island> prepareIslandForServer(String serverId) {
        /**
         * Firstly send island initial info to Island Service and create island.
         * Then add sisland to server
         * finally return
         * "Outbox transaction pattern"
         */
        String islandId = "test";

        return gameServerIslandsRepository.save(new Island(islandId, serverId));
    }

    @Override
    public Mono<Island> getGameServerInfo(String islandId) {
        return gameServerIslandsRepository.findById(islandId).map(island -> {
            return gameServerIslandsRepository.findById(island.getGameServerID()).block();
        });
    }
}
