package com.islandempires.gameserverservice.kafka;

import com.islandempires.gameserverservice.exception.CustomRunTimeException;
import com.islandempires.gameserverservice.exception.ExceptionE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
public class KafkaOutboxProducerService {

    @Value("${spring.kafka.producer.island-delete-topic}")
    private String islandTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public SendResult<String, String> sendDeleteIslandMessage(String message) {
        try {
            return this.kafkaTemplate.send(islandTopicName, message).get();
        } catch (Exception e) {
            throw new CustomRunTimeException(ExceptionE.UNEXPECTED_ERROR);
        }
    }
}
