package com.islandempires.islandservice.outbox;

import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import com.islandempires.islandservice.service.IslandModificationService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final IslandModificationService islandModificationService;

    private final KafkaOutboxProducerService kafkaOutboxProducerService;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIslandData(String islandId) throws JsonProcessingException {
        try {
            islandModificationService.delete(islandId).block();
        } catch (Exception e) {
        }
        //kafkaOutboxProducerService.sendDeleteIslandStatusMessage(islandId);
    }

}