package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.mapper.GameServerMapper;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import com.islandempires.gameserverservice.redis.RedisService;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
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
    private GameServerIslandsRepository gameServerIslandsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IslandOutboxEventRecordRepository islandOutboxEventRepository;

    @Autowired
    private GateWayClient gateWayClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GameServerMapper gameServerMapper;

    @Override
    public Mono<GameServer> initializeGameServerProperties(GameServerDTO gameServerDTO) {
        return gameServerRepository.save(modelMapper.map(gameServerDTO, GameServer.class));
    }

    @Override
    public Mono<GameServerIslands> initializeIsland(String serverId, Long userId, String jwtToken) {
        return gameServerRepository.findById(serverId).flatMap( gameServer -> {
                AllBuildings allBuildings = gameServer.getAllBuildings();
                IslandResource islandResource = gameServer.getIslandResource();
                return gateWayClient.initializeIsland(jwtToken, gameServerMapper.mapAll(allBuildings, islandResource), serverId)
                        .doOnError(e -> Mono.error(e))
                        .flatMap(islandDTO -> {
                            GameServerIslands gameServerIslands = new GameServerIslands();
                            gameServerIslands.setIslandId(islandDTO.getId());
                            gameServerIslands.setServerId(gameServer.getId());
                            gameServerIslands.setUserId(userId);
                            gameServerIslands.setCreatedDate(LocalDateTime.now());
                            return gameServerIslandsRepository.save(gameServerIslands).then(Mono.just(gameServerIslands));
                        });
        });
    }

    @Override
    @Cacheable(value = "gameServerCache", key = "#serverId")
    public GameServer getGameServerInfo(String serverId) {
        return gameServerRepository.findById(serverId).share().block();
    }

    @Override
    @Cacheable(value = "initialGameServerPropertiesDTO", key = "#serverId")
    public InitialGameServerPropertiesDTO getGameServerInitialProperties(String serverId) {
        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        return gameServerMapper.mapAll(gameServer.getAllBuildings(),
                gameServer.getIslandResource());
    }

    @Override
    public Flux<AllBuildings> getGameServerBuildingProperties() {
        return gameServerRepository.findAll().flatMap(gameServer -> {
            AllBuildings allBuildings = gameServer.getAllBuildings();
            allBuildings.setServerId(gameServer.getId());
            return Flux.just(allBuildings);
        });
    }

    @Cacheable(value = "gameServerProperties", key = "#serverId")
    public GameServer getServerBuildingInfo(String serverId) {
        GameServer gameServer = gameServerRepository.findById(serverId).share().block();
        return gameServer;
    }

    @Override
    public  Mono<GameServer> testali(String serverId) {
        return gameServerRepository.findById(serverId);
    }

}
