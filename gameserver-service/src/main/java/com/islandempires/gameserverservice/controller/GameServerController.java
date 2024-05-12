package com.islandempires.gameserverservice.controller;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.island.IslandDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.GameServerIslands;
import com.islandempires.gameserverservice.model.IslandOutboxEventRecord;
import com.islandempires.gameserverservice.service.GameServerWriteService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequestMapping("/gameservice")
public class GameServerController {

    @Autowired
    private GameServerWriteService gameServerWriteService;

    @Autowired
    private ModelMapper modelMapper;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/")
    public Mono<GameServer> initializeIslandResource(@RequestBody GameServerDTO gameServerDTO) {
        return gameServerWriteService.initializeGameServerProperties(gameServerDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/initializeIsland")
    public Mono<GameServerIslands> initializeIsland(@RequestParam("serverId") String serverId, @RequestAttribute("userId") Long userid,
                                                    @RequestHeader("Authorization") String jwtToken) {
        return gameServerWriteService.initializeIsland(serverId, userid, jwtToken);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/test1")
    public Mono<String> initializeIsland4() {
        return Mono.just("a");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/test2")
    public String initializeIsland3() {
        return "a";
    }
}
