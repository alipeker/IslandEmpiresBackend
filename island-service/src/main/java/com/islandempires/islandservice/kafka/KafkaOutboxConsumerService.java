package com.islandempires.islandservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.islandservice.service.privates.IslandModificationPrivateService;
import com.islandempires.islandservice.service.publics.IslandModificationService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final IslandModificationPrivateService islandModificationService;


    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-delete-topic']}")
    public void deleteIsland(String islandId) throws JsonProcessingException {
        try {
            islandModificationService.delete(islandId).block();
        } catch (Exception e) {
        }
    }

    @KafkaListener(topics = "db.server.island.resources.IslandResource", groupId = "debezium-consumer-group")
    public void consumeIslandResourceTopic(ConsumerRecord<String, String> record) {
        System.out.println("Received IslandResource record: " + record.value());
        // Process the record (you can parse the JSON and handle it accordingly)
    }

}