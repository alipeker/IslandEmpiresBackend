package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandOutboxEventDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.enums.OutboxEventType;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.Island;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import com.islandempires.gameserverservice.service.client.IslandBuildingServiceClient;
import com.islandempires.gameserverservice.service.client.IslandResourceServiceClient;
import com.islandempires.gameserverservice.service.client.IslandServiceClient;
import com.islandempires.gameserverservice.outbox.IslandOutboxEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerRepository.save(modelMapper.map(gameServerDTO, GameServer.class));
    }

    @Override
    public Mono<IslandDTO> initializeIsland(String serverId, Long userId) {
        IslandDTO islandDTO;

        try {
            islandDTO = islandServiceClient.create(userId);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        if (islandDTO == null || islandDTO.getId() == null) {
            throw new RuntimeException("IslandDTO or its ID is null");
        }

        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        IslandResource islandResource = gameServer.getIslandResource();
        islandResource.setIslandId(islandDTO.getId());

        try {
            islandResourceServiceClient.initializeIslandResource(userId, modelMapper.map(islandResource, IslandResourceDTO.class));

            islandBuildingServiceClient.initializeIslandBuildings(islandDTO.getId(), gameServer.getAllBuildings());
        } catch (Exception e) {
            islandOutboxEntityService.saveDeleteIslandEvent(islandDTO.getId()).share().block();
        }

        return Mono.empty();

/*

        return gameServerRepository.findById(serverId).doOnNext(gameServer -> {
            IslandResource islandResource = gameServer.getIslandResource();
            islandResource.setIslandId(islandDTO.getId());

            try {
                islandResourceServiceClient.initializeIslandResource(userId, modelMapper.map(islandResource, IslandResourceDTO.class));
            } catch (Exception e) {
                islandOutboxEntityService.saveDeleteIslandEvent(islandDTO.getId(), islandOutboxEventRepository).then(Mono.error(e));
            }
        }).then(Mono.just(islandDTO));
*/
/*
        return gameServerRepository.findById(serverId)
                .flatMap(gameServer -> {

                    /*
                    Mono.just(islandBuildingServiceClient.initializeIslandBuildings("test", gameServer.getAllBuildings())).onErrorResume(e -> {
                        return Mono.just(islandOutboxEntityService.saveDeleteIslandEvent((islandDTO.getId()))).then(Mono.error(e));
                    });

                    return  Mono.just(islandResourceServiceClient.initializeIslandResource(userId, modelMapper.map(islandResource, IslandResourceDTO.class)))
                            .flatMap(islandResourceDTO -> Mono.just(islandDTO))
                            .onErrorResume(e -> {
                                return islandOutboxEntityService.saveDeleteIslandEvent(islandDTO.getId()).then(Mono.error(e));
                            });
                });


        /*
        Mono<OtherResourceDTO> otherResource2Mono = createOtherResource2(userId, islandDTO.getId())
                .onErrorResume(e -> {
                    // OtherResource2 isteği başarısız oldu, hatayı logla ve null döndür
                    log.error("OtherResource2 isteği başarısız oldu: {}", e.getMessage());
                    return Mono.empty();
                });
*/
    }




    /*

    @Override
    public Mono<IslandDTO> initializeIsland(String serverId, Long userId) {

        IslandDTO islandDTO = null;
        try {
            islandDTO = islandServiceClient.create(userId);
        } catch (Exception e) {
            throw new RuntimeException();
        }


        return Mono.just(islandDTO)
                .flatMap(dto -> {
                    try {
                        IslandResourceDTO islandResourceDTO = new IslandResourceDTO();
                        islandResourceDTO.setIslandId(dto.getId());
                        gameServerRepository.findById("s")
                                .flatMap(gameServer -> {
                                    IslandResource islandResource = gameServer.getIslandResource();
                                    islandResource.setIslandId(dto.getId());
                                    return Mono.just(islandResourceServiceClient.initializeIslandResource(userId,
                                                    modelMapper.map(islandResource, IslandResourceDTO.class)));
                                }).onErrorResume(e -> {
                                    return islandOutboxEntityService.saveDeleteIslandEvent(dto.getId())
                                            .then(Mono.error(e));
                                }).subscribe();
                    } catch (Exception e) {
                        return islandOutboxEntityService.saveDeleteIslandEvent(dto.getId())
                                .then(Mono.error(e));
                    }
                    return Mono.just(dto);
                });

    }
*/
    @Override
    public Mono<Island> getGameServerInfo(String islandId) {
        return null;
    }
}
