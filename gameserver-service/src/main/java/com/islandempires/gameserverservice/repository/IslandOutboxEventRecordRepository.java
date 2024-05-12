package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IslandOutboxEventRecordRepository extends ReactiveCrudRepository<IslandOutboxEventRecord, String> {

}