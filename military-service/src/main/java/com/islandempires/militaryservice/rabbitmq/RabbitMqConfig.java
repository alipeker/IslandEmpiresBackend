package com.islandempires.militaryservice.rabbitmq;

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

    public static final String MOVING_TROOPS_ATTACK_EXCHANGE_NAME = "military_attack";
    public static final String MOVING_TROOPS_SUPPORT_EXCHANGE_NAME = "military_support";
    public static final String MOVING_TROOPS_RETURNING_EXCHANGE_NAME = "military_returning";
    public static final String SOLDIER_CREATE_EXCHANGE_NAME = "soldier_create";

    public static final String MOVING_TROOPS_ATTACK_QUEUE_NAME = "attackingTroopsQueue";
    public static final String MOVING_TROOPS_SUPPORT_QUEUE_NAME = "supportingTroopsQueue";
    public static final String MOVING_TROOPS_RETURNING_QUEUE_NAME = "returningTroopsQueue";
    public static final String SOLDIER_CRATE_QUEUE_NAME = "soldierCreateQueue";

    public static final String MOVING_TROOPS_ATTACK_ROUTING_KEY = "attacking_troops_routing_key";
    public static final String MOVING_TROOPS_SUPPORT_ROUTING_KEY = "supporting_troops_routing_key";
    public static final String MOVING_TROOPS_RETURNING_ROUTING_KEY = "returning_troops_routing_key";
    public static final String SOLDIER_CREATE_EXCHANGE_ROUTING_KEY= "soldier_create_routing_key";

    @Bean
    public CustomExchange movingTroopsAttackDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MOVING_TROOPS_ATTACK_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public CustomExchange movingTroopsSupportDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MOVING_TROOPS_SUPPORT_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public CustomExchange movingTroopsReturningDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(MOVING_TROOPS_RETURNING_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public CustomExchange soldierCreateDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(SOLDIER_CREATE_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue movingTroopsAttackQueue() {
        return new Queue(MOVING_TROOPS_ATTACK_QUEUE_NAME, true);
    }

    @Bean
    public Queue movingTroopsSupportQueue() {
        return new Queue(MOVING_TROOPS_SUPPORT_QUEUE_NAME, true);
    }

    @Bean
    public Queue movingTroopsReturnningQueue() {
        return new Queue(MOVING_TROOPS_RETURNING_QUEUE_NAME, true);
    }

    @Bean
    public Queue soldierCreateQueue() {
        return new Queue(SOLDIER_CRATE_QUEUE_NAME, true);
    }

    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder
                        .bind(movingTroopsAttackQueue())
                        .to(movingTroopsAttackDelayedExchange())
                        .with(MOVING_TROOPS_ATTACK_ROUTING_KEY)
                        .noargs(),
                BindingBuilder
                        .bind(movingTroopsSupportQueue())
                        .to(movingTroopsSupportDelayedExchange())
                        .with(MOVING_TROOPS_SUPPORT_ROUTING_KEY)
                        .noargs(),
                BindingBuilder
                        .bind(movingTroopsReturnningQueue())
                        .to(movingTroopsReturningDelayedExchange())
                        .with(MOVING_TROOPS_RETURNING_ROUTING_KEY)
                        .noargs(),
                BindingBuilder
                        .bind(soldierCreateQueue())
                        .to(soldierCreateDelayedExchange())
                        .with(SOLDIER_CREATE_EXCHANGE_ROUTING_KEY)
                        .noargs());
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
