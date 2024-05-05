package com.islandempires.buildingservice.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIslandData(String islandId) throws JsonProcessingException {
        System.out.println(islandId);
    }

}