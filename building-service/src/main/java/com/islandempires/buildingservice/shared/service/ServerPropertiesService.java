package com.islandempires.buildingservice.shared.service;

import com.islandempires.buildingservice.shared.building.AllBuildingsServerProperties;
import com.islandempires.buildingservice.shared.repository.AllBuildingsServerRepository;
import com.islandempires.buildingservice.shared.service.client.GameServerClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ServerPropertiesService {

    @Autowired
    private AllBuildingsServerRepository allBuildingsServerRepository;

    @Autowired
    private GameServerClient gameServerClient;

    public Mono<AllBuildingsServerProperties> get(String serverId) {
        return allBuildingsServerRepository.findById(serverId);
    }

    @PostConstruct
    public void getAllBuildingsServerProperties() {
        Flux<AllBuildingsServerProperties> allBuildingsServerProperties = gameServerClient.getGameServerBuildingProperties();

        allBuildingsServerRepository.deleteAll()
                .thenMany(allBuildingsServerProperties
                        .flatMap(allBuildingsServerRepository::save))
                .subscribe();
    }
}
