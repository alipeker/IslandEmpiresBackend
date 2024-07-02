package com.islandempires.islandservice.service.publics.impl;


import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.kafka.KafkaOutboxProducerService;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.repository.IslandRepository;
import com.islandempires.islandservice.service.publics.IslandModificationService;
import com.islandempires.islandservice.service.publics.IslandQueryService;
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
    public Mono<Boolean> isUserIslandOwner(String islandId, Long userid) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    return island.getUserId() != null && userid != null && island.getUserId() == userid;
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
