package com.islandempires.resourcesservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @Autowired
    private IslandResourceDeserializer islandResourceDeserializer;


    @KafkaListener(topics = "Resource", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(ConsumerRecord message) {
        //islandResourceDeserializer.deserialize("a", message);
        // Handle message received from Kafka topic
        System.out.println("Received message: " + islandResourceDeserializer.deserialize(message.value().toString().getBytes()));
    }
}
