package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.soldier.ShipBaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerSoldierDTO {
    private String id;

    private List<SoldierBaseInfoDTO> soldierBaseInfoList;
}
