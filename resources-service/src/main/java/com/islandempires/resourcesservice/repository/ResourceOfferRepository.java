package com.islandempires.resourcesservice.repository;

import com.islandempires.resourcesservice.model.IslandResource;
import com.islandempires.resourcesservice.model.ResourceOffer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ResourceOfferRepository extends ReactiveCrudRepository<ResourceOffer, String> {
    Flux<ResourceOffer> findByBidderIslandId(String islandId);

    Flux<ResourceOffer> findByServerId(String serverId);

    Mono<Long> countByServerId(String serverId);
}
