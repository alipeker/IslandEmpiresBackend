package com.islandempires.gameserverservice.service.impl;

import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.model.building.*;
import com.islandempires.gameserverservice.model.buildinglevelspec.AcademiaLevel;
import com.islandempires.gameserverservice.model.buildinglevelspec.HouseLevel;
import org.springframework.stereotype.Service;


@Service
public class InitializeTestGameServer {

    private void initialize() {
        GameServer gameServerDTO = new GameServer();

        gameServerDTO.setServerName("server");

        IslandResource islandResource = new IslandResource(500.0, 7, 500.0, 7,
                500.0, 7, 0.0, 1000, 2.5, 0,
                3.0, 0, 2.0, 0, 7, 7,
                0, 1.0, 1.0);

        Houses houses = new Houses();

        for(int i = 1; i <= 20; i++) {
            HouseLevel houseLevel = new HouseLevel();
            houseLevel.setLevel(i);
            houseLevel.setPopulationGrowth(Long.valueOf(i*10));
        }

    }

}
