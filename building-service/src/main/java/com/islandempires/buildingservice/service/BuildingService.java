package com.islandempires.buildingservice.service;

import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final IslandBuildingRepository islandBuildingRepository;

    public Mono<IslandBuilding> initializeIslandBuildings(String islandId, AllBuildings allBuildings) {
        return islandBuildingRepository.findById(islandId)
                .switchIfEmpty(Mono.defer(() -> {
                    IslandBuilding islandBuilding = new IslandBuilding();
                    islandBuilding.setIslandId(islandId);
                    islandBuilding.setAllBuildingList(addAllBuildingToList(allBuildings));
                    return islandBuildingRepository.save(islandBuilding);
                }))
                .flatMap(islandBuilding -> {
                    return Mono.empty();
                });
    }


    public List<BaseStructures> addAllBuildingToList(AllBuildings allBuildings) {
        if(allBuildings == null) {
            return null;
        }
        List<BaseStructures> allBuildingList = new ArrayList<>();
        allBuildingList.add(allBuildings.getAcademia());
        allBuildingList.add(allBuildings.getBarrack());
        allBuildingList.add(allBuildings.getBrickWorks());
        allBuildingList.add(allBuildings.getCannonCamp());
        allBuildingList.add(allBuildings.getClayMine());
        allBuildingList.add(allBuildings.getDairyFarm());
        allBuildingList.add(allBuildings.getDefenceTower());
        allBuildingList.add(allBuildings.getEmbassy());
        allBuildingList.add(allBuildings.getFisher());
        allBuildingList.add(allBuildings.getFoundry());
        allBuildingList.add(allBuildings.getGunsmith());
        allBuildingList.add(allBuildings.getHouses());
        allBuildingList.add(allBuildings.getIronMine());
        allBuildingList.add(allBuildings.getIslandHeadquarter());
        allBuildingList.add(allBuildings.getMill1());
        allBuildingList.add(allBuildings.getMill2());
        allBuildingList.add(allBuildings.getRiffleBarrack());
        allBuildingList.add(allBuildings.getTimberCamp1());
        allBuildingList.add(allBuildings.getTimberCamp2());
        allBuildingList.add(allBuildings.getWatchTower());
        allBuildingList.add(allBuildings.getWareHouse());
        allBuildingList.add(allBuildings.getFoodWareHouse1());
        allBuildingList.add(allBuildings.getFoodWareHouse2());
        return allBuildingList;
    }

    public Mono<Void> delete(String islandId) {
        return islandBuildingRepository.deleteByIslandId(islandId)
                .doOnError(error -> {
                    throw new RuntimeException("error");
                });
    }


}
