package com.islandempires.mapservice.repository;

import com.islandempires.mapservice.model.IslandCombined;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IslandReactiveRepository extends ReactiveElasticsearchRepository<IslandCombined, String> {
    Flux<IslandCombined> findByServerIdAndXBetweenAndYBetween(String serverId, int xStart, int xEnd, int yStart, int yEnd);

    Mono<IslandCombined> save(IslandCombined islandCombined);

}
