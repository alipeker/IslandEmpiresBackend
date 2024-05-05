package com.islandempires.gameserverservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.gameserverservice.enums.OutboxEventType;
import com.islandempires.gameserverservice.repository.IslandOutboxEventRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final IslandOutboxEventRecordRepository islandOutboxEventRecordRepository;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic-status']}")
    public void receivedMessage(String islandId) throws JsonProcessingException {
        /*
        islandOutboxEventRepository.deleteByIslandIdAndOutboxEventType(islandId, OutboxEventType.DELETE).block();*/
    }
}
