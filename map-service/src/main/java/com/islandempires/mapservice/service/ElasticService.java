package com.islandempires.mapservice.service;

import com.islandempires.mapservice.client.GatewayWebClient;
import com.islandempires.mapservice.dto.IslandDTO;
import com.islandempires.mapservice.dto.IslandResourceDTO;
import com.islandempires.mapservice.dto.UserDTO;
import com.islandempires.mapservice.model.IslandCombined;
import com.islandempires.mapservice.repository.IslandReactiveRepository;
import com.islandempires.mapservice.repository.IslandRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@AllArgsConstructor
public class ElasticService {

    private final GatewayWebClient gatewayWebClient;

    private final IslandRepository islandRepository;

    public void saveAllIslandsToElasticsearch() {
        Flux<IslandDTO> islandDTOFlux = gatewayWebClient.getAllIsland();
        Flux<IslandResourceDTO> islandResourceDTOFlux = gatewayWebClient.getAllIslandResource();
        Flux<UserDTO> userDTOFlux = gatewayWebClient.getAllUsers();

        Flux<IslandCombined> combinedFlux = islandDTOFlux.flatMap(islandDTO ->
                islandResourceDTOFlux.filter(resourceDTO -> resourceDTO.getIslandId().equals(islandDTO.getId()))
                        .flatMap(resourceDTO ->
                                userDTOFlux.filter(userDTO -> userDTO.getId().equals(islandDTO.getUserId()))
                                        .map(userDTO -> new IslandCombined(
                                                islandDTO.getId(),
                                                islandDTO.getServerId(),
                                                islandDTO.getName(),
                                                islandDTO.getUserId(),
                                                islandDTO.getX(),
                                                islandDTO.getY(),
                                                resourceDTO.getPopulation(),
                                                userDTO.getUsername()
                                        ))
                        )
        );

        combinedFlux.collectList().flatMapMany(islandCombinedList -> {
            islandRepository.saveAll(islandCombinedList);
            return Flux.fromIterable(islandCombinedList);
        }).doOnError(e -> System.out.println(e)).subscribe();
    }

    // public void saveIslandToElasticsearch(IslandCombined islandCombinedDTO) {
    //     islandRepository.deleteAll();
    //     islandRepository.save(islandCombinedDTO);
    // }

    // @Override
    // public void run(String... args) throws Exception {
    //      islandRepository.deleteAll();
    //     this.saveAllIslandsToElasticsearch();
    // }

    @Scheduled(fixedRate = 60000)
    public void performTask() {
          islandRepository.deleteAll();
         this.saveAllIslandsToElasticsearch();
    }
}
