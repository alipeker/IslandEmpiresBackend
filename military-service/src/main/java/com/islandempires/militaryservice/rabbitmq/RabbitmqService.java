package com.islandempires.militaryservice.rabbitmq;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitmqService {
    private final RabbitTemplate rabbitTemplate;

    public void sendWarAttackEndMessage(Long id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.MOVING_TROOPS_ATTACK_EXCHANGE_NAME, RabbitMqConfig.MOVING_TROOPS_ATTACK_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }

    public void sendWarSupportEndMessage(Long id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.MOVING_TROOPS_SUPPORT_EXCHANGE_NAME, RabbitMqConfig.MOVING_TROOPS_SUPPORT_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }

    public void sendWarReturningEndMessage(Long id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.MOVING_TROOPS_RETURNING_EXCHANGE_NAME, RabbitMqConfig.MOVING_TROOPS_RETURNING_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }

    public void sendSoldierProductionEndMessage(Long id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.SOLDIER_CREATE_EXCHANGE_NAME, RabbitMqConfig.SOLDIER_CREATE_EXCHANGE_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }
}
