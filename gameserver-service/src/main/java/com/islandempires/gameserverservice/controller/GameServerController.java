package com.islandempires.gameserverservice.controller;

import com.islandempires.gameserverservice.dto.GameServerDTO;
import com.islandempires.gameserverservice.dto.IslandDTO;
import com.islandempires.gameserverservice.model.GameServer;
import com.islandempires.gameserverservice.model.Island;
import com.islandempires.gameserverservice.repository.GameServerRepository;
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
    public Mono<GameServer> initializeIslandResource(@Valid @RequestBody GameServerDTO gameServerDTO) {
        return gameServerWriteService.initializeGameServerProperties(gameServerDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/initializeIsland")
    public Mono<IslandDTO> initializeIsland(@RequestParam("serverId") String serverId, @RequestHeader("userid") Long userid) {
        return gameServerWriteService.initializeIsland(serverId, userid);
    }
}
