package com.islandempires.gameserverservice.repository;

import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IslandOutboxEventRecordRepository extends ReactiveCrudRepository<IslandOutboxEventRecord, String> {


}