package com.islandempires.buildingservice.service;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.exception.CustomRunTimeException;
import com.islandempires.buildingservice.exception.ExceptionE;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.buildinglevelspec.BuildingLevel;
import com.islandempires.buildingservice.model.buildingtype.BaseStructures;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import com.islandempires.buildingservice.service.client.IslandResourceWebClientNew;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    private final IslandResourceWebClientNew islandResourceWebClientNew;


    public Mono<IslandBuilding> initializeIslandBuildings(String islandId, AllBuildings allBuildings, Long userid) {
        return islandBuildingRepository.findById(islandId)
                .switchIfEmpty(Mono.defer(() -> {
                    IslandBuilding islandBuilding = new IslandBuilding();
                    islandBuilding.setId(islandId);
                    islandBuilding.setUserId(userid);
                    islandBuilding.setAllBuildingList(addAllBuildingToList(allBuildings));
                    return islandBuildingRepository.save(islandBuilding);
                }))
                .flatMap(islandBuilding -> {
                    return Mono.just(islandBuilding);
                });
    }

    public Mono<Void> increaseIslandBuildingLvl(String islandId, IslandBuildingEnum islandBuildingEnum, String token, Long userid) {
        IslandBuilding islandBuilding = this.islandBuildingRepository.findById(islandId).share().block();

        if(islandBuilding.getUserId() != userid) {
            throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
        }

        BaseStructures baseStructures = islandBuilding.getAllBuildingList().stream()
                .filter(structure -> structure != null && structure.getIslandBuildingEnum().equals(islandBuildingEnum))
                .findFirst()
                .orElseThrow(() -> new CustomRunTimeException(ExceptionE.NOT_FOUND));

        int initialLvl = baseStructures.getInitialLvl();

        Flux<BuildingScheduledTask> buildingScheduledTaskFlux = buildingScheduledTaskRepository.findByIslandBuildingEnumAndIslandId(islandBuildingEnum, islandId);
        List<BuildingScheduledTask> buildingScheduledTaskList = buildingScheduledTaskFlux.share().collectList().share().block();

        if(buildingScheduledTaskList.size() > 0) {
            initialLvl = buildingScheduledTaskList.get(buildingScheduledTaskList.size() - 1).getNextLvl().getLevel();
        }

        if(baseStructures == null || baseStructures.getBuildingLevelList() == null
                || baseStructures.getBuildingLevelList().size() > 0 || initialLvl == baseStructures.getBuildingLevelList().size()) {
            Mono.error(new Throwable());
        }

        BuildingLevel initialLevel = 0 < initialLvl ?
                baseStructures.getBuildingLevelList().get(initialLvl - 1)
                : null;

        BuildingLevel nextLevel = baseStructures.getBuildingLevelList().get(initialLvl);


        BuildingScheduledTask buildingScheduledTask = new BuildingScheduledTask(islandId, baseStructures.getIslandBuildingEnum(), initialLevel, nextLevel, nextLevel.getConstructionDuration());

        buildingScheduledTask.setLastCalculatedTimestamp(System.currentTimeMillis());

        return islandResourceWebClientNew.assignResources(islandId, nextLevel.getRawMaterialsAndPopulationCost(), token)
                .onErrorResume(throwable -> {
                    return Mono.error(throwable);
                })
                .then(buildingScheduledTaskRepository.save(buildingScheduledTask))
                .then(Mono.empty());

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
