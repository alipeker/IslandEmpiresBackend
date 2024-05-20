package com.islandempires.islandservice.service.impl;


import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.kafka.KafkaOutboxProducerService;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.repository.IslandRepository;
import com.islandempires.islandservice.service.IslandModificationService;
import com.islandempires.islandservice.service.IslandQueryService;
import com.islandempires.islandservice.service.client.GateWayClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class IslandServiceImpl implements IslandQueryService, IslandModificationService {
    @Autowired
    private IslandRepository islandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KafkaOutboxProducerService kafkaOutboxProducerService;

    @Autowired
    private GateWayClient gateWayClient;

    @Override
    public Mono<IslandDTO> get(String islandId, Long userid) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    if(island.getUserId() != null && island.getUserId() != userid) {
                       new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(island, IslandDTO.class);
                });
    }

    @Override
    public Flux<Island> getAll() {
        return islandRepository.findAll().take(40);
    }

    @Override
    public Mono<IslandDTO> getIsland(String islandId, Long userid) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    if(island.getUserId() != null && island.getUserId() != userid) {
                        new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(island, IslandDTO.class);
                });
    }

    @Override
    public Mono<Boolean> isUserIslandOwner(String islandId, Long userid) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    return island.getUserId() != null && userid != null && island.getUserId() == userid;
                });
    }

    @Override
    public Mono<IslandDTO> create(Long userId, InitialGameServerPropertiesDTO initialGameServerPropertiesDTO, String jwtToken, String serverId) {
        IslandDTO islandDTO = new IslandDTO();
        islandDTO.setName("test");
        islandDTO.setUserId(userId);
        islandDTO.setX(new Random().nextInt(100) + 1);
        islandDTO.setY(new Random().nextInt(100) + 1);
        islandDTO.setServerId(serverId);
        return islandRepository.save(modelMapper.map(islandDTO, Island.class)).flatMap(island -> {
            return Mono.zip(gateWayClient.initializeIslandBuildings(island.getId(), initialGameServerPropertiesDTO.getInitialAllBuildings(), jwtToken, serverId).doOnError(e -> Mono.error(e)),
                            gateWayClient.initializeIslandResource(island.getId(), initialGameServerPropertiesDTO.getIslandResource(), jwtToken, serverId).doOnError(e -> Mono.error(e)))
                    .doOnError(e -> {
                        kafkaOutboxProducerService.sendDeleteIslandEvent(island.getId());
                        Mono.error(e);
                    })
                    .flatMap(Tuple -> {
                        return Mono.just(modelMapper.map(island, IslandDTO.class));
                    });
            });
    }

    @Override
    public Mono<IslandDTO> updateName(Long userId, String islandID, String newName) {
        return islandRepository.findById(islandID)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    if(island.getUserId() != null && island.getUserId() != userId) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    island.setName(newName);
                    return island;
                }).flatMap(island -> islandRepository.save(island))
                .map(island -> modelMapper.map(island, IslandDTO.class));
    }

    @Override
    public Mono<IslandDTO> updateOwner(Long userId, String islandID) {
        return islandRepository.findById(islandID)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    island.setUserId(userId);
                    return island;
                }).flatMap(island -> islandRepository.save(island))
                .map(island -> modelMapper.map(island, IslandDTO.class));
    }

    @Override
    public Mono<Void> delete(String islandId) {
        return islandRepository.deleteById(islandId)
                .doOnError(error -> {
                    throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
                });
    }
}
