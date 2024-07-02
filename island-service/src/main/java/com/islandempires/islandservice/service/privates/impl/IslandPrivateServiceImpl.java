package com.islandempires.islandservice.service.privates.impl;


import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.kafka.KafkaOutboxProducerService;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.repository.IslandRepository;
import com.islandempires.islandservice.service.client.GateWayClient;
import com.islandempires.islandservice.service.privates.IslandModificationPrivateService;
import com.islandempires.islandservice.service.privates.IslandQueryPrivateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class IslandPrivateServiceImpl implements IslandQueryPrivateService, IslandModificationPrivateService {
    @Autowired
    private IslandRepository islandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KafkaOutboxProducerService kafkaOutboxProducerService;

    @Autowired
    private GateWayClient gateWayClient;

    @Override
    public Mono<IslandDTO> get(String islandId) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    return modelMapper.map(island, IslandDTO.class);
                });
    }

    @Override
    public Flux<Island> getAll() {
        return islandRepository.findAll().take(40);
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
    public Mono<IslandDTO> create(Long userId, InitialGameServerPropertiesDTO initialGameServerPropertiesDTO, String serverId) {
        IslandDTO islandDTO = new IslandDTO();
        islandDTO.setName("test");
        islandDTO.setUserId(userId);
        islandDTO.setX(new Random().nextInt(100) + 1);
        islandDTO.setY(new Random().nextInt(100) + 1);
        islandDTO.setServerId(serverId);
        return islandRepository.save(modelMapper.map(islandDTO, Island.class)).flatMap(island -> {
            return Mono.zip(gateWayClient.initializeIslandBuildings(island.getId(), initialGameServerPropertiesDTO.getInitialAllBuildings(), userId, serverId).doOnError(Mono::error),
                            gateWayClient.initializeIslandResource(island.getId(), initialGameServerPropertiesDTO.getIslandResource(), userId, serverId).doOnError(Mono::error),
                            gateWayClient.initializeIslandMilitary(island.getId(), userId, serverId).doOnError(Mono::error))
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

    @Override
    public Mono<Double> calculateDistance(String islandId1, String islandId2) {
        Mono<Island> island1Mono = islandRepository.findById(islandId1);
        Mono<Island> island2Mono = islandRepository.findById(islandId2);

        return Mono.zip(island1Mono, island2Mono).flatMap(Tuple -> {
                    Island island1 = Tuple.getT1();
                    Island island2 = Tuple.getT2();

                    int x1 = island1.getX();
                    int y1 = island1.getY();
                    int x2 = island2.getX();
                    int y2 = island2.getY();
                    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

                    return Mono.just(distance);
                });
    }
}
