package com.islandempires.sessiontrackingservice.repository;

import com.islandempires.sessiontrackingservice.model.IslandRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface IslandRecordRepository extends MongoRepository<IslandRecord, String> {
    public void deleteByLocalDateTimeBefore(LocalDateTime dateTime);
}
