package com.islandempires.islandservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.islandservice.exception.CustomRunTimeException;
import com.islandempires.islandservice.exception.ExceptionE;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaOutboxProducerService {
    @Value("${spring.kafka.producer.island-delete-topic}")
    private String islandTopicName;

    @Value("${spring.kafka.producer.server-user-delete-topic}")
    private String serverUserTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public CompletableFuture<Void> sendDeleteIslandEvent(String islandId) {
        return CompletableFuture.runAsync(() -> {
            this.kafkaTemplate.send(islandTopicName, islandId);
        });
    }

    public CompletableFuture<Void> sendDeleteServerUserEvent(String serverId, Long userId) {
        try {
            return CompletableFuture.runAsync(() -> {
                kafkaTemplate.send(serverUserTopicName, serverId + "/" + userId);
            });
        } catch (Exception e) {

            throw new CustomRunTimeException(ExceptionE.NOT_FOUND);
        }
    }
}
