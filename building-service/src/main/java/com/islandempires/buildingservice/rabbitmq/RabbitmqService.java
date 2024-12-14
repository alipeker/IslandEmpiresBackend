package com.islandempires.buildingservice.rabbitmq;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class RabbitmqService {
    private final RabbitTemplate rabbitTemplate;

    public void sendBuildingEndMessage(String id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.BUILDING_EXCHANGE_NAME, RabbitMqConfig.BUILDING_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }

}
