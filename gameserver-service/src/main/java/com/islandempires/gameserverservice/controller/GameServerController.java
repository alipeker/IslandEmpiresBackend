package com.islandempires.gameserverservice.controller;

import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.GameServerSoldier;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("gameservice/public")
public class GameServerController {

    @Autowired
    private GameServerWriteService gameServerWriteService;

    @Autowired
    private GameServerReadService gameServerReadService;

    @Autowired
    private ModelMapper modelMapper;


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/initializeIsland")
    public Mono<GameServerIslands> initializeIsland(@RequestParam("serverId") String serverId, @RequestAttribute("userId") Long userid) {
        return gameServerWriteService.initializeIsland(serverId, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerInfo/{serverId}")
    public GameServer getGameServerInfo(@PathVariable String serverId) {
        return gameServerReadService.getGameServerInfo(serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getServerBuildingsInfo/{serverId}")
    public Object getServerBuildingInfo(@PathVariable String serverId) {
        return gameServerReadService.getServerBuildingInfo(serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerInitialProperties/{serverId}")
    public InitialGameServerPropertiesDTO getGameServerInitialProperties(@PathVariable String serverId) {
        return gameServerReadService.getGameServerInitialProperties(serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerBuildingProperties")
    public Flux<AllBuildings> getGameServerBuildingProperties() {
        return gameServerReadService.getGameServerBuildingProperties();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerSoldierProperties")
    public Flux<GameServerSoldier> getGameServerSoldierProperties() {
        return gameServerReadService.getGameServerSoldierProperties();
    }

}
