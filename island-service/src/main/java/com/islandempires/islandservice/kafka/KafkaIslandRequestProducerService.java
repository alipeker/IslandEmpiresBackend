package com.islandempires.islandservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaIslandRequestProducerService {
    @Value("${spring.kafka.producer.island-request-topic}")
    private String islandTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<Void> sendGetIslandRequestMessage(String islandId) {
        return CompletableFuture.runAsync(() -> {
            this.kafkaTemplate.send(islandTopicName, islandId);
        });
    }
}
