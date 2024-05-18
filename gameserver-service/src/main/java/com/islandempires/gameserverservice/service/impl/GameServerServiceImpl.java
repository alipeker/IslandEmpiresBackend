package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.InitializeIslandResponseDTO;
import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.exception.CustomRunTimeException;
import com.islandempires.gameserverservice.exception.ExceptionE;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import com.islandempires.gameserverservice.service.client.GateWayClient;
import com.islandempires.gameserverservice.service.client.IslandBuildingServiceClient;
import com.islandempires.gameserverservice.service.client.IslandResourceServiceClient;
import com.islandempires.gameserverservice.service.client.IslandServiceClient;
import com.islandempires.gameserverservice.outbox.IslandOutboxEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameServerServiceImpl implements GameServerWriteService, GameServerReadService {

    @Autowired
    private GameServerRepository gameServerRepository;

    @Autowired
    private GameServerIslandsRepository gameServerIslandsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IslandOutboxEntityService islandOutboxEntityService;

    @Autowired
    private IslandServiceClient islandServiceClient;

    @Autowired
    private IslandResourceServiceClient islandResourceServiceClient;

    @Autowired
    private IslandBuildingServiceClient islandBuildingServiceClient;

    @Autowired
    private IslandOutboxEventRecordRepository islandOutboxEventRepository;

    @Autowired
    private GateWayClient gateWayClient;

    @Override
    public Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerRepository.save(modelMapper.map(gameServerDTO, GameServer.class));
    }

    @Override
    public Mono<GameServerIslands> initializeIsland2(String serverId, Long userId, String jwtToken) {
        IslandDTO islandDTO;
        try {
            islandDTO = gateWayClient.initializeIsland(jwtToken).share().block();
        } catch (Exception e) {
            throw new CustomRunTimeException(ExceptionE.UNEXPECTED_ERROR);
        }


        if (islandDTO == null || islandDTO.getId() == null) {
            throw new CustomRunTimeException(ExceptionE.UNEXPECTED_ERROR);
        }

        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        IslandResource islandResource = gameServer.getIslandResource();
        islandResource.setIslandId(islandDTO.getId());

        try {
            gateWayClient.initializeIslandBuildings(islandDTO.getId(), gameServer.getAllBuildings(), jwtToken);

            gateWayClient.initializeIslandResource(modelMapper.map(islandResource, IslandResourceDTO.class), jwtToken);

            GameServerIslands gameServerIslands = new GameServerIslands();
            gameServerIslands.setIslandId(islandDTO.getId());
            gameServerIslands.setServerId(gameServer.getId());
            gameServerIslands.setUserId(userId);
            gameServerIslands.setCreatedDate(LocalDateTime.now());
            return gameServerIslandsRepository.save(gameServerIslands).then(Mono.just(gameServerIslands));
        } catch (Exception e) {
            islandOutboxEntityService.saveDeleteIslandEvent(islandDTO.getId()).share().block();
            throw new CustomRunTimeException(ExceptionE.UNEXPECTED_ERROR);
        }
    }

    /*
    @Override
    public Mono<GameServerIslands> initializeIsland(String serverId, Long userId, String jwtToken) {
        GameServerIslands gameServerIslands = new GameServerIslands();
        gameServerIslands.setIslandId("s");
        gameServerIslands.setServerId("6641508811318644f6e0985f");
        gameServerIslands.setUserId(userId);
        gameServerIslands.setCreatedDate(LocalDateTime.now());
        return gameServerIslandsRepository.save(gameServerIslands);
    }*/


    @Override
    public Mono<GameServerIslands> initializeIsland(String serverId, Long userId, String jwtToken) {
        IslandDTO islandDTO = gateWayClient.initializeIsland2(jwtToken);
        if (islandDTO == null || islandDTO.getId() == null) {
            throw new CustomRunTimeException(ExceptionE.UNEXPECTED_ERROR);
        }
        return gameServerRepository.findById(serverId)
                .flatMap(gameServer -> {
                    IslandResource islandResource = gameServer.getIslandResource();
                    islandResource.setIslandId(islandDTO.getId());

                    IslandBuildingDTO islandBuildingDTOMono =
                            gateWayClient.initializeIslandBuildings2(islandDTO.getId(), gameServer.getAllBuildings(), jwtToken);


                    IslandResourceDTO islandResourceDTOMono =
                            gateWayClient.initializeIslandResource2(modelMapper.map(islandResource, IslandResourceDTO.class), jwtToken);

                    GameServerIslands gameServerIslands = new GameServerIslands();
                    gameServerIslands.setIslandId(islandDTO.getId());
                    gameServerIslands.setServerId(gameServer.getId());
                    gameServerIslands.setUserId(userId);
                    gameServerIslands.setCreatedDate(LocalDateTime.now());
                    return gameServerIslandsRepository.save(gameServerIslands);
                });
    }




    @Override
    public Mono<GameServerIslands> getGameServerInfo(String islandId) {
        return null;
    }
}
