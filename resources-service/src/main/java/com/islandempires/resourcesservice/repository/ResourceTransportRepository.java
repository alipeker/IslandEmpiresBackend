package com.islandempires.resourcesservice.repository;


import com.islandempires.resourcesservice.model.ResourceTransport;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceTransportRepository extends ReactiveCrudRepository<ResourceTransport, String> {

}
