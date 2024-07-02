package com.islandempires.militaryservice.converter;

import com.islandempires.militaryservice.dto.SoldierBaseInfoDTO;
import com.islandempires.militaryservice.model.soldier.ShipBaseInfo;
import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import com.islandempires.militaryservice.model.GameServerSoldier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SoldierBaseInfoConverter {

    public static SoldierBaseInfo convertToEntity(SoldierBaseInfoDTO dto, GameServerSoldier gameServerSoldier) {
        SoldierBaseInfo soldierBaseInfo = new SoldierBaseInfo();
        soldierBaseInfo.setSoldierSubTypeName(dto.getId());
        soldierBaseInfo.setAttackPoint(dto.getAttackPoint());
        soldierBaseInfo.setDefensePoints(dto.getDefensePoints());
        soldierBaseInfo.setRawMaterialsAndPopulationCost(dto.getRawMaterialsAndPopulationCost());
        soldierBaseInfo.setProductionDuration(dto.getProductionDuration());
        soldierBaseInfo.setGameServerSoldier(gameServerSoldier);
        return soldierBaseInfo;
    }

    public List<SoldierBaseInfo> convertToEntityList(List<SoldierBaseInfoDTO> dtoList, GameServerSoldier gameServerSoldier) {
        return dtoList.stream()
                .map(dto -> convertToEntity(dto, gameServerSoldier))
                .collect(Collectors.toList());
    }

    public static ShipBaseInfo convertToShipEntity(SoldierBaseInfoDTO dto, GameServerSoldier gameServerSoldier) {
        ShipBaseInfo shipBaseInfo = new ShipBaseInfo();
        shipBaseInfo.setSoldierCapacityOfShip(dto.getSoldierCapacityOfShip());
        shipBaseInfo.setCanonCapacityOfShip(dto.getCanonCapacityOfShip());
        shipBaseInfo.setTotalLootCapacity(dto.getTotalLootCapacity());
        shipBaseInfo.setTimeToTraverseMapCell(dto.getTimeToTraverseMapCell());
        shipBaseInfo.setShipSubTypeName(dto.getId());
        shipBaseInfo.setGameServerSoldier(gameServerSoldier);
        return shipBaseInfo;
    }

    public List<ShipBaseInfo> convertToShipEntityList(List<SoldierBaseInfoDTO> dtoList, GameServerSoldier gameServerSoldier) {
        return dtoList.stream()
                .map(dto -> convertToShipEntity(dto, gameServerSoldier))
                .collect(Collectors.toList());
    }
}
