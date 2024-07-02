package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.exception.CustomRunTimeException;
import com.islandempires.gameserverservice.exception.ExceptionE;
import com.islandempires.gameserverservice.mapper.GameServerMapper;
import com.islandempires.gameserverservice.model.*;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import com.islandempires.gameserverservice.repository.*;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import com.islandempires.gameserverservice.service.client.GateWayClient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameServerServiceImpl implements GameServerWriteService, GameServerReadService {

    @Autowired
    private GameServerRepository gameServerRepository;

    @Autowired
    private GameServerIslandResourceRepository gameServerIslandResourceRepository;

    @Autowired
    private GameServerAllBuildingsRepository gameServerAllBuildingsRepository;

    @Autowired
    private GameServerSoldierRepository gameServerSoldierRepository;

    @Autowired
    private GameServerIslandsRepository gameServerIslandsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IslandOutboxEventRecordRepository islandOutboxEventRepository;

    @Autowired
    private GateWayClient gateWayClient;


    @Autowired
    private GameServerMapper gameServerMapper;

    @Override
    public Mono<Void> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerIslandResourceRepository.save(
                new GameServerIslandResource(gameServerDTO.getId(), gameServerDTO.getIslandResource(), LocalDateTime.now()))
                .flatMap(e -> gameServerAllBuildingsRepository.save(
                        new GameServerAllBuildings(gameServerDTO.getId(), gameServerDTO.getAllBuildings(), LocalDateTime.now())))
                .flatMap(e -> gameServerSoldierRepository.save(
                        new GameServerSoldier(gameServerDTO.getId(), gameServerDTO.getSoldierBaseInfoList(), LocalDateTime.now())
                )).then(Mono.empty());
    }

    @Override
    public Mono<GameServerIslands> initializeIsland(String serverId, Long userId) {
        return gameServerRepository.findById(serverId).flatMap( gameServer -> {
                AllBuildings allBuildings = gameServer.getAllBuildings();
                IslandResource islandResource = gameServer.getIslandResource();

                return gameServerIslandsRepository.existsByServerIdAndUserId(serverId, userId).flatMap(alreadyExist -> {
                    if(Boolean.TRUE.equals(alreadyExist)) {
                        throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
                    }

                    return gateWayClient.initializeIsland(gameServerMapper.mapAll(allBuildings, islandResource), serverId, userId)
                            .doOnError(Mono::error)
                            .flatMap(islandDTO -> {
                                GameServerIslands gameServerIslands = new GameServerIslands();
                                gameServerIslands.setIslandId(islandDTO.getId());
                                gameServerIslands.setServerId(gameServer.getId());
                                gameServerIslands.setUserId(userId);
                                gameServerIslands.setCreatedDate(LocalDateTime.now());
                                return gameServerIslandsRepository.save(gameServerIslands).then(Mono.just(gameServerIslands));
                            });
                });

        });
    }

    @Override
    public GameServer getGameServerInfo(String serverId) {
        return gameServerRepository.findById(serverId).share().block();
    }

    @Override
    public InitialGameServerPropertiesDTO getGameServerInitialProperties(String serverId) {
        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        return gameServerMapper.mapAll(gameServer.getAllBuildings(),
                gameServer.getIslandResource());
    }

    @Override
    public Flux<AllBuildings> getGameServerBuildingProperties() {
        return gameServerAllBuildingsRepository.findAll().flatMap(gameServer -> {
            AllBuildings allBuildings = gameServer.getAllBuildings();
            allBuildings.setServerId(gameServer.getId());
            return Flux.just(allBuildings);
        });
    }

    @Override
    public Flux<GameServerSoldier> getGameServerSoldierProperties() {
        return gameServerSoldierRepository.findAll();
    }

    public GameServer getServerBuildingInfo(String serverId) {
        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        return gameServer;
    }

    @Override
    public  Mono<GameServer> testali(String serverId) {
        return gameServerRepository.findById(serverId);
    }

}
