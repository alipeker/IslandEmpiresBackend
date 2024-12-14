package com.islandempires.websocketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitialData {

    private Object clanUser;

    private List<Object> islandMilitaries;

    private List<Object> islandBuildings;

    private List<Object> islandResources;

    private List<Object> userIslands;

    private Object gameServerInfo;

}
