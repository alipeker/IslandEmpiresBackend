package com.islandempires.gateway;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;

public class RouterValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/signin",
            "/auth/signup",
            "/gameservice/getServerBuildingsInfo",
            "/gameservice/getGameServerBuildingProperties",
            "/gameservice/getGameServerSoldierProperties"
    );

    public static final String websocketPathPrefix = "/websocket/";

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri)) 
                    && !request.getURI().getPath().startsWith(websocketPathPrefix);
}
