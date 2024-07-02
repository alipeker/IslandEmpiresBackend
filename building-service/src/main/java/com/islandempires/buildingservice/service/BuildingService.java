package com.islandempires.buildingservice.service;

import com.islandempires.buildingservice.dto.IslandResourceDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.exception.CustomRunTimeException;
import com.islandempires.buildingservice.exception.ExceptionE;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.building.Building;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import com.islandempires.buildingservice.service.client.IslandResourceWebClientNew;
import com.islandempires.buildingservice.shared.buildingtype.BaseStructures;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import com.islandempires.buildingservice.shared.service.ServerPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static com.islandempires.buildingservice.util.FindInListWithField.findBuilding;
import static com.islandempires.buildingservice.util.FindInListWithField.findBuildingProperties;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    private final IslandResourceWebClientNew islandResourceWebClientNew;

    private final ServerPropertiesService serverPropertiesService;

    public Mono<IslandBuilding> get(String islandId, Long userid) {
        return islandBuildingRepository.findById(islandId)
                .flatMap(islandBuilding -> {
                    if(islandBuilding.getUserId() != userid) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return Mono.just(islandBuilding);
                });
    }

    public Mono<IslandBuilding> initializeIslandBuildings(String serverId, String islandId, AllBuildings allBuildings, Long userid) {
        return islandBuildingRepository.findById(islandId)
                .switchIfEmpty(Mono.defer(() -> {
                    IslandBuilding islandBuilding = new IslandBuilding();
                    islandBuilding.setId(islandId);
                    islandBuilding.setUserId(userid);
                    islandBuilding.setAllBuilding(allBuildings);
                    islandBuilding.setServerId(serverId);
                    return islandBuildingRepository.save(islandBuilding);
                }))
                .flatMap(islandBuilding -> {
                    return Mono.just(islandBuilding);
                });
    }


    public Mono<IslandResourceDTO> increaseIslandBuildingLvl(String islandId, IslandBuildingEnum islandBuildingEnum, Long userid) {
        return this.islandBuildingRepository.findById(islandId).flatMap(islandBuilding -> {
                    if(!islandBuilding.getUserId().equals(userid)) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    Building building = findBuilding(islandBuilding.getAllBuilding(), islandBuildingEnum);
                    if(building != null) {
                        return serverPropertiesService.get(islandBuilding.getServerId()).flatMap(allBuildingsServerProperties -> {
                            BaseStructures buildingProperties = findBuildingProperties(allBuildingsServerProperties, islandBuildingEnum);


                            Flux<BuildingScheduledTask> buildingScheduledTaskFlux = buildingScheduledTaskRepository.findByIslandBuildingEnumAndIslandId(islandBuildingEnum, islandId);

                            return buildingScheduledTaskFlux.collectList().flatMap(buildingScheduledTasks -> {
                                if(buildingProperties == null || buildingProperties.getBuildingLevelList() == null
                                        || buildingProperties.getBuildingLevelList().size() == 0
                                        || buildingProperties.getBuildingLevelList().size() == (building.getLvl() + buildingScheduledTasks.size())) {
                                    throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
                                }
                                int initialLvl = (buildingScheduledTasks.size() > 0) ? building.getLvl() + buildingScheduledTasks.size() : building.getLvl();
                                int nextLvl = initialLvl + 1;
                                RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost = buildingProperties.getBuildingLevelList().get(initialLvl).getRawMaterialsAndPopulationCost();

                                BuildingScheduledTask buildingScheduledTask =
                                        new BuildingScheduledTask(allBuildingsServerProperties.getServerId(), islandId, buildingProperties.getIslandBuildingEnum(), initialLvl, nextLvl,
                                                buildingProperties.getBuildingLevelList().get(initialLvl).getConstructionDuration(), rawMaterialsAndPopulationCost);

                                buildingScheduledTask.setLastCalculatedTimestamp(System.currentTimeMillis());

                                return islandResourceWebClientNew.assignResources(islandId, rawMaterialsAndPopulationCost)
                                        .flatMap(islandResourceDTO -> buildingScheduledTaskRepository.save(buildingScheduledTask)
                                                .thenReturn(islandResourceDTO))
                                        .doOnError(Mono::error);
                            });
                        });
                    }
                    return Mono.error(Throwable::new);
                }).switchIfEmpty(Mono.error(Throwable::new));
    }


    public Mono<Void> increaseIslandBuildingLvlDone(String islandId, IslandBuildingEnum islandBuildingEnum, int newLvl) {
        return this.islandBuildingRepository.findById(islandId).flatMap(islandBuilding -> {
            Building building = findBuilding(islandBuilding.getAllBuilding(), islandBuildingEnum);
            building.setLvl(newLvl);
            return this.islandBuildingRepository.save(islandBuilding);
        }).switchIfEmpty(Mono.error(Throwable::new)).then();
    }

    public Mono<Void> delete(String islandId) {
        return islandBuildingRepository.deleteById(islandId)
                .doOnError(error -> {
                    throw new RuntimeException("error");
                });
    }


}
