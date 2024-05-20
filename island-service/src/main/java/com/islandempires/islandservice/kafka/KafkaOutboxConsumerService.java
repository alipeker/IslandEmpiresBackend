package com.islandempires.islandservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.islandservice.service.IslandModificationService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final IslandModificationService islandModificationService;


    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIsland(String islandId) throws JsonProcessingException {
        try {
            islandModificationService.delete(islandId).block();
        } catch (Exception e) {
        }
    }

}