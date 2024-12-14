package com.islandempires.resourcesservice.rabbitmq;

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

    public static final String RESOURCE_TRANSPORT_EXCHANGE_NAME = "resourceTransport";
    public static final String RESOURCE_TRANSPORT_QUEUE_NAME = "resourceTransportQueue";
    public static final String RESOURCE_TRANSPORT_ROUTING_KEY = "resourceTransport_routing_key";

    public static final String RESOURCE_TRANSPORT_RETURNING_EXCHANGE_NAME = "resourceTransportReturning";
    public static final String RESOURCE_TRANSPORT_RETURNING_QUEUE_NAME = "resourceTransportReturningQueue";
    public static final String RESOURCE_TRANSPORT_RETURNING_ROUTING_KEY = "resourceTransport_Returning_routing_key";


    public static final String RESOURCE_TRANSPORT_CANCELED_EXCHANGE_NAME = "resourceTransportCanceled";
    public static final String RESOURCE_TRANSPORT_CANCELED_QUEUE_NAME = "resourceTransportCanceledQueue";
    public static final String RESOURCE_TRANSPORT_CANCELED_ROUTING_KEY = "resourceTransport_Canceled_Returning_routing_key";


    @Bean
    public CustomExchange resourceTransportDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RESOURCE_TRANSPORT_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue resourceTransportQueue() {
        return new Queue(RESOURCE_TRANSPORT_QUEUE_NAME, true);
    }

    @Bean
    public CustomExchange resourceTransportReturningDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RESOURCE_TRANSPORT_RETURNING_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue resourceTransportReturningQueue() {
        return new Queue(RESOURCE_TRANSPORT_RETURNING_QUEUE_NAME, true);
    }


    @Bean
    public CustomExchange resourceTransportCanceledDelayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RESOURCE_TRANSPORT_CANCELED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue resourceTransportCanceledQueue() {
        return new Queue(RESOURCE_TRANSPORT_CANCELED_QUEUE_NAME, true);
    }

    @Bean
    public Declarables bindings() {
        return new Declarables(
                BindingBuilder
                        .bind(resourceTransportQueue())
                        .to(resourceTransportDelayedExchange())
                        .with(RESOURCE_TRANSPORT_ROUTING_KEY)
                        .noargs(),
                BindingBuilder
                        .bind(resourceTransportReturningQueue())
                        .to(resourceTransportReturningDelayedExchange())
                        .with(RESOURCE_TRANSPORT_RETURNING_ROUTING_KEY)
                        .noargs(),
                BindingBuilder
                        .bind(resourceTransportCanceledQueue())
                        .to(resourceTransportCanceledDelayedExchange())
                        .with(RESOURCE_TRANSPORT_CANCELED_ROUTING_KEY)
                        .noargs()
        );
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
