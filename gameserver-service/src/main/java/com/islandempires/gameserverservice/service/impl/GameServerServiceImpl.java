package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.IslandBuildingDTO;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.exception.CustomRunTimeException;
import com.islandempires.gameserverservice.exception.ExceptionE;
import com.islandempires.gameserverservice.mapper.GameServerMapper;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.redis.RedisService;
import com.islandempires.gameserverservice.repository.GameServerIslandsRepository;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import com.islandempires.gameserverservice.service.client.GateWayClient;
import com.islandempires.gameserverservice.outbox.IslandOutboxEntityService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
        return gateWayClient.initializeIsland(jwtToken).flatMap(islandDTO -> {
            return gameServerRepository.findById(serverId).flatMap( gameServer -> {

                return Mono.zip(gateWayClient.initializeIslandBuildings(islandDTO.getId(), gameServer.getAllBuildings(), jwtToken)
                                        .doOnError(e -> Mono.error(e)),
                        gateWayClient.initializeIslandResource(islandDTO.getId(), modelMapper.map(gameServer.getIslandResource(), IslandResourceDTO.class), jwtToken))
                        .doOnError(e -> Mono.error(e))
                        .flatMap(Tuple -> {
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


    // sadece island service istek at başarılı olursa burada game server islands kaydet
    @Override
    public Mono<GameServerIslands> initializeIsland2(String serverId, Long userId, String jwtToken) {
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
    public  Mono<GameServer> testali(String serverId) {
        return gameServerRepository.findById(serverId);
    }

}
