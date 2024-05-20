package com.islandempires.gameserverservice.mapper;

import com.islandempires.gameserverservice.dto.initial.InitialAllBuildingsDTO;
import com.islandempires.gameserverservice.dto.initial.InitialBuildingDTO;
import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import com.islandempires.gameserverservice.dto.island.IslandResourceDTO;
import com.islandempires.gameserverservice.model.IslandResource;
import com.islandempires.gameserverservice.model.building.AllBuildings;
import com.islandempires.gameserverservice.model.buildingtype.BaseStructures;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServerMapper {

    @Autowired
    private ModelMapper modelMapper;

    public InitialGameServerPropertiesDTO mapAll(AllBuildings allBuildings, IslandResource islandResource) {
        InitialAllBuildingsDTO initialAllBuildingsDTO = mapAllBuildingsToInitialAllBuildingsDTO(allBuildings);
        IslandResourceDTO islandResourceDTO = mapIslandResourceToDTO(islandResource);
        return new InitialGameServerPropertiesDTO(initialAllBuildingsDTO, islandResourceDTO);
    }

    public InitialAllBuildingsDTO mapAllBuildingsToInitialAllBuildingsDTO(AllBuildings allBuildings) {
        InitialAllBuildingsDTO initialAllBuildingsDTO = new InitialAllBuildingsDTO();
        initialAllBuildingsDTO.setAcademia(mapIfNotNull(allBuildings.getAcademia()));
        initialAllBuildingsDTO.setBarrack(mapIfNotNull(allBuildings.getBarrack()));
        initialAllBuildingsDTO.setBrickWorks(mapIfNotNull(allBuildings.getBrickWorks()));
        initialAllBuildingsDTO.setCannonCamp(mapIfNotNull(allBuildings.getCannonCamp()));
        initialAllBuildingsDTO.setClayMine(mapIfNotNull(allBuildings.getClayMine()));
        initialAllBuildingsDTO.setDairyFarm1(mapIfNotNull(allBuildings.getDairyFarm1()));
        initialAllBuildingsDTO.setDairyFarm2(mapIfNotNull(allBuildings.getDairyFarm2()));
        initialAllBuildingsDTO.setDefenceTower(mapIfNotNull(allBuildings.getDefenceTower()));
        initialAllBuildingsDTO.setEmbassy(mapIfNotNull(allBuildings.getEmbassy()));
        initialAllBuildingsDTO.setFisher(mapIfNotNull(allBuildings.getFisher()));
        initialAllBuildingsDTO.setFoundry(mapIfNotNull(allBuildings.getFoundry()));
        initialAllBuildingsDTO.setGunsmith(mapIfNotNull(allBuildings.getGunsmith()));
        initialAllBuildingsDTO.setHouses(mapIfNotNull(allBuildings.getHouses()));
        initialAllBuildingsDTO.setIronMine(mapIfNotNull(allBuildings.getIronMine()));
        initialAllBuildingsDTO.setIslandHeadquarter(mapIfNotNull(allBuildings.getIslandHeadquarter()));
        initialAllBuildingsDTO.setMill1(mapIfNotNull(allBuildings.getMill1()));
        initialAllBuildingsDTO.setMill2(mapIfNotNull(allBuildings.getMill2()));
        initialAllBuildingsDTO.setRiffleBarrack(mapIfNotNull(allBuildings.getRiffleBarrack()));
        initialAllBuildingsDTO.setTimberCamp1(mapIfNotNull(allBuildings.getTimberCamp1()));
        initialAllBuildingsDTO.setTimberCamp2(mapIfNotNull(allBuildings.getTimberCamp2()));
        initialAllBuildingsDTO.setWatchTower(mapIfNotNull(allBuildings.getWatchTower()));
        initialAllBuildingsDTO.setWareHouse(mapIfNotNull(allBuildings.getWareHouse()));
        initialAllBuildingsDTO.setFoodWareHouse1(mapIfNotNull(allBuildings.getFoodWareHouse1()));
        initialAllBuildingsDTO.setFoodWareHouse2(mapIfNotNull(allBuildings.getFoodWareHouse2()));

        return initialAllBuildingsDTO;
    }


    private InitialBuildingDTO mapIfNotNull(BaseStructures building) {
        if(building == null) {
            return null;
        }
        return modelMapper.map(building, new TypeToken<InitialBuildingDTO>() {}.getType());
    }

    public IslandResourceDTO mapIslandResourceToDTO(IslandResource islandResource) {
        return modelMapper.map(islandResource, IslandResourceDTO.class);
    }
}
