package com.islandempires.resourcesservice.service;

import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.repository.IslandResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class IslandResourceService {
    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private KafkaTemplate<Object, List<IslandResource>> kafkaTemplate;


    public Flux<IslandResource> getAll() {
        System.out.println(this.islandResourceRepository.count().block());
        return this.islandResourceRepository.findAll();
    }

    public Mono<IslandResource> get(String islandId) {
        /*
        List<IslandResource> testl = new ArrayList<>();
        for(int i = 1; i < 1000001; i++) {
            UUID uuid = UUID.randomUUID();
            IslandResource test = new IslandResource();
            test.setLastCalculatedTimestamp(System.currentTimeMillis());
            test.setWood(Double.valueOf(0));
            test.setHourlyWoodProduction(Double.valueOf(i) * 5);
            test.setIslandid(uuid.toString());
            testl.add(test);
        }
        return this.islandResourceRepository.saveAll(testl);*/
        return this.islandResourceRepository.findById(islandId);
    }

    public Mono<IslandResource> save(IslandResource islandResource) {
        return this.islandResourceRepository.save(islandResource);
    }

    public Mono<IslandResource> update(IslandResource islandResource) {
        return this.islandResourceRepository.findById(islandResource.getIslandid())
                .map(updatedResource -> {
                    updatedResource.setClay(islandResource.getClay());
                    return updatedResource;
                })
                .flatMap(this.islandResourceRepository::save);
    }
}
