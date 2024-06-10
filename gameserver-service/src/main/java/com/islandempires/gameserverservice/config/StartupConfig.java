package com.islandempires.gameserverservice.config;

import com.islandempires.gameserverservice.repository.GameServerRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class StartupConfig {

    @Autowired
    private GameServerRepository gameServerRepository;


}