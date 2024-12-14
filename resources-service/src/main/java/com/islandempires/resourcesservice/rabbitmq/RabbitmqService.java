package com.islandempires.resourcesservice.rabbitmq;

import com.islandempires.resourcesservice.enums.BidderAcceptorEnum;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitmqService {
    private final RabbitTemplate rabbitTemplate;

    public void sendTradingMessage(String id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESOURCE_TRANSPORT_EXCHANGE_NAME, RabbitMqConfig.RESOURCE_TRANSPORT_RETURNING_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }

    public void sendTradingReturningMessage(String id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESOURCE_TRANSPORT_RETURNING_EXCHANGE_NAME, RabbitMqConfig.RESOURCE_TRANSPORT_RETURNING_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }


    public void sendTradingCanceledMessage(String id, long delayInMillis) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESOURCE_TRANSPORT_CANCELED_EXCHANGE_NAME, RabbitMqConfig.RESOURCE_TRANSPORT_CANCELED_ROUTING_KEY, id,
                message -> {
                    message.getMessageProperties().setDelay((int) delayInMillis);
                    return message;
                });
    }
}
