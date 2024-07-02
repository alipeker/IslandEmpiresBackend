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
public class BuildingPrivateService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    private final IslandResourceWebClientNew islandResourceWebClientNew;

    private final ServerPropertiesService serverPropertiesService;

    public Mono<IslandBuilding> get(String islandId) {
        return islandBuildingRepository.findById(islandId)
                .flatMap(Mono::just);
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
                .flatMap(Mono::just);
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
