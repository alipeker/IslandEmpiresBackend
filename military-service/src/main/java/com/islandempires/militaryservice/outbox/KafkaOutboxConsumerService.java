package com.islandempires.militaryservice.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.militaryservice.service.WarService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final WarService warService;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIsland(String islandId) throws JsonProcessingException {
        try {
            warService.delete(islandId);
        } catch (Exception e) {
        }
    }

}
