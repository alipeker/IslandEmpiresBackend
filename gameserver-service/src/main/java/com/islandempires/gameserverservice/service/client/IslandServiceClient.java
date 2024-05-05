package com.islandempires.gameserverservice.service.client;

import com.islandempires.gameserverservice.dto.island.IslandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@FeignClient(value = "island", url = "${urls.island}")
public interface IslandServiceClient {
    @PostMapping(value = "/island/")
    IslandDTO create(@RequestHeader("userid") Long userid);

    @DeleteMapping(value = "/island/{islandId}")
    IslandDTO rollBackIslandCreate(@RequestParam String islandId);
}
