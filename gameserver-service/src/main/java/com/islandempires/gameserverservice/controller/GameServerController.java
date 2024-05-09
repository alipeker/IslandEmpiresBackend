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
    @PatchMapping("/initializeIsland")
    public Mono<GameServerIslands> initializeIsland(@RequestParam("serverId") String serverId, @RequestHeader("userid") Long userid) {
        return gameServerWriteService.initializeIsland(serverId, userid);
    }
}
