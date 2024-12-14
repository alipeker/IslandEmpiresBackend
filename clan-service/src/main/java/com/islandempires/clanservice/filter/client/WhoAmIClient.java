package com.islandempires.clanservice.filter.client;

import com.islandempires.clanservice.dto.GameServerResponseDTO;
import com.islandempires.clanservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.islandempires.clanservice.dto.clan.ClanDTO;
import com.islandempires.clanservice.dto.user.UserPrivateDTO;

import java.util.List;
import java.util.HashMap;

@FeignClient(name = "gatewayClient", url = "${urls.gateway}")
public interface WhoAmIClient {

    @GetMapping(value = "/auth/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    Long whoami(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken);

    @GetMapping("/gameservice/private/getGameServers")
    List<GameServerResponseDTO> getGameServerSoldierProperties();

    @GetMapping("/auth/private/getUser/{userId}")
    UserDTO getUser(@PathVariable Long userId);

    @PostMapping("/private/websocket/clan/{clanId}")
    void sendClanChange(@PathVariable Long clanId, @RequestBody ClanDTO clan) ;

    @PostMapping("/private/websocket/clans")
    void sendClansChange(@RequestBody HashMap<Long, ClanDTO> clans);

    @PostMapping("/private/websocket/clanUser/{serverId}/{userId}")
    void sendClanUserChange(@PathVariable String serverId, @PathVariable Long userId, @RequestBody UserPrivateDTO clanUser);

    @PostMapping("/private/websocket/clanDelete/{clanId}")
    void sendDeleteClanEvent(Long clanId);

}
