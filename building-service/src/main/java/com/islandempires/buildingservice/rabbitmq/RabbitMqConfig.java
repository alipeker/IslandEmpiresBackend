package com.islandempires.buildingservice.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String BUILDING_EXCHANGE_NAME = "building";
    public static final String BUILDING_QUEUE_NAME = "buildingQueue";
    public static final String BUILDING_ROUTING_KEY = "building_routing_key";

    @Bean
    public CustomExchange movingTroopsAttackDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(BUILDING_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue movingTroopsAttackQueue() {
        return new Queue(BUILDING_QUEUE_NAME, true);
    }


    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder
                        .bind(movingTroopsAttackQueue())
                        .to(movingTroopsAttackDelayedExchange())
                        .with(BUILDING_ROUTING_KEY)
                        .noargs());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
