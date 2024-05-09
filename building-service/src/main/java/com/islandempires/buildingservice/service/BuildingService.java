package com.islandempires.buildingservice.service;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.Academia;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.buildinglevelspec.AcademiaLevel;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import com.islandempires.buildingservice.model.resources.RawMaterialsAndPopulationCost;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import com.islandempires.buildingservice.service.client.IslandResourceServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    @Autowired
    private final IslandResourceServiceClient islandResourceServiceClient;


    public Mono<IslandBuilding> initializeIslandBuildings(String islandId, AllBuildings allBuildings, Long userid) {
        return islandBuildingRepository.findById(islandId)
                .switchIfEmpty(Mono.defer(() -> {
                    IslandBuilding islandBuilding = new IslandBuilding();
                    islandBuilding.setId(islandId);
                    islandBuilding.setAllBuildingList(addAllBuildingToList(allBuildings));
                    return islandBuildingRepository.save(islandBuilding);
                }))
                .flatMap(islandBuilding -> {
                    return Mono.empty();
                });
    }

    public Mono<Void> increaseIslandBuildingLvl(String islandId, IslandBuildingEnum islandBuildingEnum, Long userid) {
        IslandBuilding islandBuilding = this.islandBuildingRepository.findById(islandId).share().block();

        BaseStructures baseStructures = islandBuilding.getAllBuildingList().stream()
                .filter(structure -> structure != null && structure.getIslandBuildingEnum().equals(islandBuildingEnum))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Bina bulunamadı veya uygun türde değil."));


        if(baseStructures == null || baseStructures.getBuildingLevelList() == null
                || baseStructures.getBuildingLevelList().size() > 0 || baseStructures.getInitialLvl() == baseStructures.getBuildingLevelList().size()) {
            Mono.error(new Throwable());
        }

        BuildingLevel initialLevel = 0 < baseStructures.getInitialLvl() ?
                baseStructures.getBuildingLevelList().get(baseStructures.getInitialLvl() - 1)
                : null;

        BuildingLevel nextLevel = baseStructures.getBuildingLevelList().get(baseStructures.getInitialLvl());

        // check raw material request to resource service
        RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost = nextLevel.getRawMaterialsAndPopulationCost();

        BuildingScheduledTask buildingScheduledTask = new BuildingScheduledTask(islandId, baseStructures.getIslandBuildingEnum(), initialLevel, nextLevel, nextLevel.getConstructionDuration());
        // Building change next lvl specs request here

        return Mono.just(islandResourceServiceClient.assignResources(islandId, rawMaterialsAndPopulationCost))
                .then(buildingScheduledTaskRepository.save(buildingScheduledTask).then(Mono.empty()));
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
        allBuildingList.add(allBuildings.getDairyFarm1());
        allBuildingList.add(allBuildings.getDairyFarm2());
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
        return islandBuildingRepository.deleteById(islandId)
                .doOnError(error -> {
                    throw new RuntimeException("error");
                });
    }


}
