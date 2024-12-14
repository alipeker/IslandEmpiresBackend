package com.islandempires.gameserverservice.controller;

import com.islandempires.gameserverservice.dto.request.IslandCreateRequestDTO;
import com.islandempires.gameserverservice.dto.response.GameServerSoldierDTO;
import com.islandempires.gameserverservice.dto.response.ServerUserRegistrationInfoDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerAllBuildings;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.service.GameServerReadService;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<GameServerIslands> initializeIsland(@RequestParam("serverId") String serverId, @RequestAttribute("userId") Long userid,
                                                    @RequestBody IslandCreateRequestDTO islandCreateRequestDTO) {
        return gameServerWriteService.initializeIsland(islandCreateRequestDTO, serverId, userid);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getUserServers")
    public Flux<ServerUserRegistrationInfoDTO> getUserServers(@RequestAttribute("userId") Long userid) {
        return gameServerReadService.getUserServers(userid);
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
    public Mono<GameServer> getGameServerInitialProperties(@PathVariable String serverId) {
        return gameServerReadService.getGameServerInitialProperties(serverId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerBuildingProperties")
    public Flux<GameServerAllBuildings> getGameServerBuildingProperties() {
        return gameServerReadService.getGameServerBuildingProperties();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getGameServerSoldierProperties")
    public Flux<GameServerSoldierDTO> getGameServerSoldierProperties() {
        return gameServerReadService.getGameServerSoldierProperties();
    }

}
