package com.islandempires.websocketservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@Controller
@RequestMapping("/private/websocket")
@AllArgsConstructor
public class ApiController {

    private final WebSocketController webSocketController;

    @PostMapping("/{serverId}/{userId}/{islandId}")
    @ResponseStatus(HttpStatus.OK)
    private void sendBuildingDone(@PathVariable String serverId, @PathVariable Long userId, @PathVariable String islandId,
                                  @RequestBody Object islandBuilding) {
        webSocketController.sendBuildingDone(serverId, userId, islandId, islandBuilding);
    }

    @PostMapping("/military/{serverId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    private void sendIslandMilitaryChange(@PathVariable String serverId, @PathVariable Long userId, @RequestBody Object islandMilitary) {
        webSocketController.sendIslandMilitaryChange(serverId, userId, islandMilitary);
    }

    @PostMapping("/clan/{clanId}")
    @ResponseStatus(HttpStatus.OK)
    private void sendClanChange(@PathVariable Long clanId, @RequestBody Object clan) {
        webSocketController.sendClanChange(clanId, clan);
    }

    @PostMapping("/clans")
    @ResponseStatus(HttpStatus.OK)
    private void sendClansChange(@RequestBody HashMap<Long, Object> clans) {
        webSocketController.sendClansChange(clans);
    }

    @PostMapping("/clanUser/{serverId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    private void sendClanUserChange(@PathVariable String serverId, @PathVariable Long userId, @RequestBody Object clanUser) {
        webSocketController.sendClanUserChange(serverId, userId, clanUser);
    }

    @PostMapping("/clanDelete/{clanId}")
    @ResponseStatus(HttpStatus.OK)
    private void sendDeleteClanEvent(@PathVariable Long clanId) {
        webSocketController.sendDeleteClanEvent(clanId);
    }
}
