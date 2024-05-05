package com.islandempires.gameserverservice.outbox;


import com.islandempires.gameserverservice.enums.OutboxEventType;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class IslandOutboxEntityService {

    @Autowired
    private IslandOutboxEventRecordRepository islandOutboxEventRecordRepository;


    public Mono<IslandOutboxEventRecord> saveDeleteIslandEvent(String islandId) {
        IslandOutboxEventRecord islandOutboxEventRecord = new IslandOutboxEventRecord();
        islandOutboxEventRecord.setIslandId(islandId);
        islandOutboxEventRecord.setOutboxEventType(OutboxEventType.DELETE);
        return islandOutboxEventRecordRepository.save(islandOutboxEventRecord);
    }
}
