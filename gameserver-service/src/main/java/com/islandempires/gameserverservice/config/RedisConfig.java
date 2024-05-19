package com.islandempires.gameserverservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.model.GameServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, InitialGameServerPropertiesDTO> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                           RedisSerializer<InitialGameServerPropertiesDTO> gameServerRedisSerializer) {
        RedisTemplate<String, InitialGameServerPropertiesDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new StringRedisSerializer());
        template.setValueSerializer(gameServerRedisSerializer);
        return template;
    }

    @Bean
    public RedisSerializer<InitialGameServerPropertiesDTO> gameServerRedisSerializer() {
        return new InitialGameServerPropertiesDTOSerializer();
    }
}