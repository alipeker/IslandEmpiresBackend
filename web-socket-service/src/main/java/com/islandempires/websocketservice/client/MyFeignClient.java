package com.islandempires.websocketservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "webSocketClient", url = "${urls.gateway}")
public interface MyFeignClient {

    @GetMapping("/military/private/getIslandsWithUserId/{userId}/{serverId}")
    List<Object> getIslandMilitaries(@PathVariable("userId") Long userId, @PathVariable("serverId") String serverId);

    @GetMapping("/building/private/getUserBuildings/{userId}/{serverId}")
    List<Object> getUserBuildings(@PathVariable("userId") Long userId, @PathVariable("serverId") String serverId);

    @GetMapping("/resource/private/getByUserId/{userId}/{serverId}")
    List<Object> getUserResources(@PathVariable("userId") Long userId, @PathVariable("serverId") String serverId);

    @GetMapping("/clan/private/getUserAll/{userId}/{serverId}")
    Object getUserClan(@PathVariable("userId") Long userId, @PathVariable("serverId") String serverId);

    @GetMapping("/gameservice/private/getGameServerInfo/{serverId}")
    Object getGameServerInfo(@PathVariable("serverId") String serverId);

    @GetMapping("/island/private/getAllUserIslands/{serverId}/{userId}")
    List<Object> getAllUserIslands(@PathVariable("serverId") String serverId, @PathVariable("userId") Long userId);
}
