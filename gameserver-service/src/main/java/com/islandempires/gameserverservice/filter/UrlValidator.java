package com.islandempires.gameserverservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class UrlValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/gameservice/getGameServerBuildingProperties",
            "/gameservice/getGameServerSoldierProperties"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
