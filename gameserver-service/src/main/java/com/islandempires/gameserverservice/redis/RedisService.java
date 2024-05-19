package com.islandempires.gameserverservice.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.gameserverservice.model.GameServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RedisService {


    public void save(GameServer gameServer) {
        /*
        redisTemplate.opsForValue().set(gameServer.getId(), gameServer);*/
    }

    public GameServer find(String key) {
        /*
        ObjectMapper objectMapper = new ObjectMapper();
        return redisTemplate.opsForValue().get(key);*/
        return null;
    }

    public void delete(String key) {
        /*
        redisTemplate.delete(key);*/
    }


    @Scheduled(fixedRateString ="10000")
    public void scheduleFixedRateTask() {
        System.out.println();
    }
}
