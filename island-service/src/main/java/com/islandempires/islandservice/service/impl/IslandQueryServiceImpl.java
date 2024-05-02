package com.islandempires.islandservice.service.impl;


import com.islandempires.islandservice.dto.IslandDTO;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.model.Island;
import com.islandempires.islandservice.repository.IslandRepository;
import com.islandempires.islandservice.service.IslandModificationService;
import com.islandempires.islandservice.service.IslandQueryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class IslandQueryServiceImpl implements IslandQueryService, IslandModificationService {
    @Autowired
    private IslandRepository islandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Mono<IslandDTO> get(String islandId) {
        return islandRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> modelMapper.map(island, IslandDTO.class));
    }

    @Override
    public Mono<IslandDTO> create(Long userId) {
        IslandDTO islandDTO = new IslandDTO();
        islandDTO.setName("My Island");
        islandDTO.setUserId(userId);
        islandDTO.setX(new Random().nextInt(100) + 1);
        islandDTO.setY(new Random().nextInt(100) + 1);
        return islandRepository.save(modelMapper.map(islandDTO, Island.class))
                .map(island -> modelMapper.map(island, IslandDTO.class));
    }

    @Override
    public Mono<IslandDTO> updateName(Long userId, String islandID, String newName) {
        return islandRepository.findById(islandID)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(island -> {
                    if(island.getUserId() != null && island.getUserId() != userId) {
                        throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
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
    public Mono<Boolean> delete(String islandId) {
        return islandRepository.deleteById(islandId).map(island -> {return true;});
    }
}
