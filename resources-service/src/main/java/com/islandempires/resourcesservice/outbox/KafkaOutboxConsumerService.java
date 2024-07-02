package com.islandempires.resourcesservice.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.resourcesservice.service.privates.IslandResourceModificationPrivateService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final IslandResourceModificationPrivateService islandResourceModificationService;


    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIsland(String islandId) throws JsonProcessingException {
        try {
            islandResourceModificationService.delete(islandId).block();
        } catch (Exception e) {
        }
    }

}