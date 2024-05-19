package com.islandempires.gameserverservice.config;

import com.islandempires.gameserverservice.redis.RedisService;
import com.islandempires.gameserverservice.repository.GameServerRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.publisher.Flux;

@Configuration
public class StartupConfig {

    @Autowired
    private GameServerRepository gameServerRepository;

    @Autowired
    private RedisService redisService;


    @PostConstruct
    @Scheduled(fixedDelayString = "5000")
    public void scheduleFixedRateTask() {
        gameServerRepository.findAll()
                .flatMap(gameServer -> {
                    redisService.save(gameServer);
                    return Flux.just(gameServer);
                })
                .subscribe();
    }
}