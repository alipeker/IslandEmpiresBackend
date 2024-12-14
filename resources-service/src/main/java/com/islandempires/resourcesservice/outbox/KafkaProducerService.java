package com.islandempires.resourcesservice.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;

@Service
public class KafkaProducerService {
    @Value("${spring.kafka.producer.island-population-change-topic}")
    private String islandTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public Mono<Void> publishPopulationChange(String islandId) {
        return Mono.fromRunnable(() -> {
            this.kafkaTemplate.send(islandTopicName, islandId);
        });
    }

}
