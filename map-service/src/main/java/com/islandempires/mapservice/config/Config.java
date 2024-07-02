package com.islandempires.mapservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@EnableReactiveElasticsearchRepositories
public class Config  {
/*
    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        return ReactiveRestClients.create(ClientConfiguration.localhost());
    }

    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        return ReactiveRestClients.create("http://localhost:9200");
    }*/
}