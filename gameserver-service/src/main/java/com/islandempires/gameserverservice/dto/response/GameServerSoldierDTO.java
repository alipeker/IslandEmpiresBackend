package com.islandempires.gameserverservice.dto.response;

import com.islandempires.gameserverservice.model.GameServerSoldier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerSoldierDTO implements Serializable {
    private String id;

    private GameServerSoldier gameServerSoldier;
}
