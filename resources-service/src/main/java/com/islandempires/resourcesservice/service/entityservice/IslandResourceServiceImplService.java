package com.islandempires.resourcesservice.service.entityservice;

import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import com.islandempires.resourcesservice.service.IslandMetricsCalculatorService;
import com.islandempires.resourcesservice.service.IslandResourceCalculatorService;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class IslandResourceServiceImplService implements IslandResourceQueryService {
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandResourceCalculatorService islandResourceCalculatorService;

    @Autowired
    private IslandMetricsCalculatorService islandMetricsCalculatorService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Mono<IslandResourceDTO> get(String islandId, Long userId) {
        return this.islandResourceRepository.findById(islandId)
                .switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)))
                .map(islandResource -> {
                    if(userId != islandResource.getUserId()) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return modelMapper.map(islandResource, IslandResourceDTO.class);
                });
    }


    @Override
    public Mono<Boolean> validateResourceAllocationForIsland(String islandId, ResourceAllocationRequestDTO resourceAllocationRequestDTO, Long userId){
        return islandResourceRepository.findById(islandId)
                .map(islandResource -> {
                    if(userId != islandResource.getUserId()) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return islandResourceCalculatorService.checkResourceAllocation(islandResource, resourceAllocationRequestDTO);
                }).switchIfEmpty(Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND)));
    }


}
