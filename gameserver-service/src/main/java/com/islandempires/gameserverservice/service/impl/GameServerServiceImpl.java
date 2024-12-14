package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.dto.request.IslandCreateRequestDTO;
import com.islandempires.gameserverservice.dto.response.GameServerIdDTO;
import com.islandempires.gameserverservice.dto.response.GameServerSoldierDTO;
import com.islandempires.gameserverservice.dto.response.ServerUserRegistrationInfoDTO;
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
        GameServer gameServer = new GameServer();
        gameServer.setServerName(gameServerDTO.getServerName());
        gameServer.setId(gameServerDTO.getId());
        gameServer.setLocalDateTime(LocalDateTime.now());
        gameServer.setGameServerAllBuildings(new GameServerAllBuildings(gameServerDTO.getId(), gameServerDTO.getAllBuildings(), LocalDateTime.now()));
        gameServer.setGameServerIslandResource(new GameServerIslandResource(gameServerDTO.getIslandResource(), LocalDateTime.now()));
        gameServer.setGameServerSoldier(new GameServerSoldier(gameServerDTO.getSoldierBaseInfoList(), LocalDateTime.now()));
        return gameServerRepository.save(gameServer).then(Mono.empty());
    }

    @Override
    public Mono<GameServerIslands> initializeIsland(IslandCreateRequestDTO islandCreateRequestDTO, String serverId, Long userId) {
        return gameServerRepository.findById(serverId).flatMap(gameServer -> {
                AllBuildings allBuildings = gameServer.getGameServerAllBuildings().getAllBuildings();
                IslandResource islandResource = gameServer.getGameServerIslandResource().getIslandResource();

                return gameServerIslandsRepository.existsByServerIdAndUserId(serverId, Long.valueOf(0)).flatMap(alreadyExist -> {
                    System.out.println(alreadyExist);
                    if(Boolean.TRUE.equals(alreadyExist)) {
                        throw new CustomRunTimeException(ExceptionE.ALREADY_EXIST);
                    }

                    InitialGameServerPropertiesDTO initialGameServerPropertiesDTO = gameServerMapper.mapAll(allBuildings, islandResource);
                    initialGameServerPropertiesDTO.setIslandCreateRequestDTO(islandCreateRequestDTO);
                    return gateWayClient.initializeIsland(initialGameServerPropertiesDTO, serverId, userId)
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
    public Mono<GameServer> getGameServerInitialProperties(String serverId) {
        return gameServerRepository.findById(serverId);
    }

    @Override
    public Flux<GameServerAllBuildings> getGameServerBuildingProperties() {
        return gameServerRepository.findAll()
                .flatMap(gameServer -> {
                    GameServerAllBuildings gameServerAllBuildings = gameServer.getGameServerAllBuildings();
                    gameServerAllBuildings.setServerId(gameServer.getId());
                    return Flux.just(gameServerAllBuildings);
                });
    }
    @Override
    public Flux<GameServerSoldierDTO> getGameServerSoldierProperties() {
        return gameServerRepository.findAll()
                .flatMap(gameServer -> Flux.just(new GameServerSoldierDTO(gameServer.getId(), gameServer.getGameServerSoldier())));
    }

    public Mono<GameServerSoldier> getServerBuildingInfo(String serverId) {
        return gameServerRepository.findById(serverId)
                .flatMap(gameServer -> Mono.just(gameServer.getGameServerSoldier()));
    }

    @Override
    public  Mono<GameServer> testali(String serverId) {
        return gameServerRepository.findById(serverId);
    }

    public Flux<String> findAllServerIds() {
        return gameServerRepository.findAll().flatMap(gameServer -> Flux.just(gameServer.getId()));
    }

    @Override
    public Flux<ServerUserRegistrationInfoDTO> getUserServers(Long userId) {
        Flux<String> serverIdFlux = gameServerRepository.findAllByOrderByLocalDateTimeDesc()
                .map(GameServer::getId);

        Flux<String> userRegisterServerIdListFlux = gameServerIslandsRepository.findByUserId(userId)
                .map(GameServerIslands::getServerId);

        return serverIdFlux
                .flatMap(serverId -> userRegisterServerIdListFlux
                        .collectList()
                        .flatMapMany(userRegisterServerIdList -> Flux.just(new ServerUserRegistrationInfoDTO(
                                serverId,
                                userRegisterServerIdList.contains(serverId),
                                LocalDateTime.now()))
                        )
                );
    }


    @Override
    public Flux<GameServerIdDTO> getGameServers() {
        return gameServerRepository.findAll()
                .flatMap(gameServer -> {
                    return Mono.just(new GameServerIdDTO(gameServer.getId(), true));
                });
    }
}
