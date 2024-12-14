package com.islandempires.buildingservice.shared.service;

import com.islandempires.buildingservice.shared.BuildingServerProperties;
import com.islandempires.buildingservice.shared.repository.BuildingsServerPropertiesRepository;
import com.islandempires.buildingservice.shared.service.client.GameServerClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ServerPropertiesService {

    @Autowired
    private BuildingsServerPropertiesRepository buildingsServerPropertiesRepository;

    @Autowired
    private GameServerClient gameServerClient;

    public Mono<BuildingServerProperties> get(String serverId) {
        return buildingsServerPropertiesRepository.findByServerId(serverId);
    }

    @PostConstruct
    public void getAllBuildingsServerProperties() {
        Flux<BuildingServerProperties> allBuildingsServerProperties = gameServerClient.getGameServerBuildingProperties();


        buildingsServerPropertiesRepository.deleteAll()
                .thenMany(allBuildingsServerProperties
                        .flatMap(buildingsServerPropertiesRepository::save))
                .subscribe();
    }
}
