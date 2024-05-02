package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.IslandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "island", url = "${urls.island}")
public interface IslandServiceClient {
    @PostMapping(value = "/island/{userid}")
    IslandDTO create(@RequestHeader("userid") Long userid);

    @DeleteMapping(value = "/island/{islandId}")
    IslandDTO rollBackIslandCreate(@RequestParam String islandId);
}
