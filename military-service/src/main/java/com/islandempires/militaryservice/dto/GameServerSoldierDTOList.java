package com.islandempires.militaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerSoldierDTOList {
    private List<SoldierBaseInfoDTO> soldierBaseInfoList;
}
