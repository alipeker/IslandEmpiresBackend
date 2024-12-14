package com.islandempires.buildingservice.service;

import com.islandempires.buildingservice.dto.IslandResourceDTO;
import com.islandempires.buildingservice.dto.IslandScheduledJobAndResourceDTO;
import com.islandempires.buildingservice.dto.IslandBuildingAndScheduledJobDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.enums.SoldierSubTypeEnum;
import com.islandempires.buildingservice.exception.CustomRunTimeException;
import com.islandempires.buildingservice.exception.ExceptionE;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.building.Building;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.rabbitmq.RabbitmqService;
import com.islandempires.buildingservice.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import com.islandempires.buildingservice.service.client.GatewayWebClientService;
import com.islandempires.buildingservice.shared.building.Gunsmith;
import com.islandempires.buildingservice.shared.buildingtype.BaseStructures;
import com.islandempires.buildingservice.shared.requirement.SoldierRequirement;
import com.islandempires.buildingservice.shared.resources.RawMaterialsAndPopulationCost;
import com.islandempires.buildingservice.shared.service.ServerPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;


import java.util.HashMap;
import java.util.List;

import static com.islandempires.buildingservice.util.FindInListWithField.findBuilding;


@Service
@RequiredArgsConstructor
public class BuildingService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    private final GatewayWebClientService gatewayWebClientService;

    private final ServerPropertiesService serverPropertiesService;

    private final RabbitmqService rabbitmqService;

    public Mono<IslandBuildingAndScheduledJobDTO> get(String islandId, Long userid) {
        Flux<BuildingScheduledTask> buildingScheduledTaskFlux = buildingScheduledTaskRepository.findByIslandId(islandId);
        Mono<IslandBuilding> islandBuildingMono = islandBuildingRepository.findById(islandId);

        return islandBuildingMono.zipWith(buildingScheduledTaskFlux.collectList(), (islandBuilding, buildingScheduledTasks) -> {
            if(!islandBuilding.getUserId().equals(userid)) {
                throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
            }
            IslandBuildingAndScheduledJobDTO dto = new IslandBuildingAndScheduledJobDTO();
            dto.setIslandBuilding(islandBuilding);
            List<BuildingScheduledTask> buildingScheduledTaskList = buildingScheduledTasks
                                                                        .stream()
                                                                        .sorted((task1, task2) -> Long.compare(task1.getStartingDateTimestamp(), task2.getStartingDateTimestamp()))
                                                                        .toList();
            dto.setBuildingScheduledTaskList(buildingScheduledTaskList);
            return dto;
        });
    }

    public Flux<BuildingScheduledTask> getScheduledJobs(String islandId, Long userid) {
        return buildingScheduledTaskRepository.findByIslandId(islandId)
                .flatMap(buildingScheduledTask -> {
                    if(!buildingScheduledTask.getUserId().equals(userid)) {
                        throw new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES);
                    }
                    return Mono.just(buildingScheduledTask);
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
                .flatMap(Mono::just);
    }


    public Mono<IslandScheduledJobAndResourceDTO> increaseIslandBuildingLvl(String islandId, IslandBuildingEnum islandBuildingEnum, Long userid) {
        return buildingScheduledTaskRepository.findByIslandId(islandId).collectList()
                .flatMap(buildingScheduledTaskList -> {
                    if (buildingScheduledTaskList != null && buildingScheduledTaskList.stream().filter(task -> !task.getException()).count() >= 2) {
                        return Mono.error(new CustomRunTimeException(ExceptionE.ALREADY_EXIST));
                    }
                    return islandBuildingRepository.findById(islandId).flatMap(islandBuilding -> {
                        if (!islandBuilding.getUserId().equals(userid)) {
                            return Mono.error(new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES));
                        }
                        Building building = islandBuilding.getAllBuilding().findBuildingWithEnum(islandBuildingEnum);
                        if (building != null) {
                            return serverPropertiesService.get(islandBuilding.getServerId()).flatMap(allBuildingsServerProperties -> {
                                BaseStructures buildingProperties = allBuildingsServerProperties.getAllBuildings().findBuildingWithEnum(islandBuildingEnum);
                                HashMap<IslandBuildingEnum, Integer> upgradeConditions = buildingProperties.getUpgradeConditions();
                                for(IslandBuildingEnum islandBuildingEnumCondition : upgradeConditions.keySet()) {
                                    if(upgradeConditions.get(islandBuildingEnumCondition) == null) {
                                        continue;
                                    }
                                    if(islandBuilding.getAllBuilding().findBuildingWithEnum(islandBuildingEnumCondition).getInitialLvl() <
                                            upgradeConditions.get(islandBuildingEnumCondition)) {
                                        throw new CustomRunTimeException(ExceptionE.BUILDING_CONDITIONS_ERROR);
                                    }
                                }

                                return buildingScheduledTaskRepository.findByIslandBuildingEnumAndIslandId(islandBuildingEnum, islandId).collectList()
                                        .flatMap(buildingScheduledTasks -> {
                                            if (buildingProperties.getBuildingLevelList() == null ||
                                                    buildingProperties.getBuildingLevelList().size() == 0 ||
                                                    buildingProperties.getBuildingLevelList().size() == (building.getInitialLvl() + buildingScheduledTasks.size())) {
                                                return Mono.error(new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES));
                                            }

                                            int initialLvl = (buildingScheduledTasks.size() > 0) ? building.getInitialLvl() + buildingScheduledTasks.size() : building.getInitialLvl();
                                            int nextLvl = initialLvl + 1;
                                            RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost = buildingProperties.getBuildingLevelList().get(initialLvl).getRawMaterialsAndPopulationCost();

                                            Duration duration = buildingProperties.getBuildingLevelList().get(initialLvl).getConstructionDuration();
                                            Duration finalDuration = multiplyDuration(duration, (double) islandBuilding.getTimeReductionPercentage() / 100);

                                            if(islandBuilding.getRefundTime().toMillis() > 0) {
                                                finalDuration = islandBuilding.minusRefundTime(finalDuration);
                                                BuildingScheduledTask buildingScheduledTask = new BuildingScheduledTask(
                                                        allBuildingsServerProperties.getServerId(), islandId, buildingProperties.getIslandBuildingEnum(), initialLvl, nextLvl,
                                                        finalDuration, rawMaterialsAndPopulationCost, userid);

                                                buildingScheduledTask.setLastCalculatedTimestamp(System.currentTimeMillis());

                                                return islandBuildingRepository.save(islandBuilding)
                                                                .flatMap(islandBuilding1 -> {
                                                                    return gatewayWebClientService.assignResources(islandId, rawMaterialsAndPopulationCost)
                                                                            .flatMap(islandResourceDTO -> {
                                                                                return recordScheduledJob(buildingScheduledTask)
                                                                                        .flatMap(buildingScheduledTaskReturn -> {
                                                                                            IslandScheduledJobAndResourceDTO islandScheduledJobAndResourceDTO = new IslandScheduledJobAndResourceDTO();
                                                                                            islandScheduledJobAndResourceDTO.setIslandResourceDTO(islandResourceDTO);
                                                                                            islandScheduledJobAndResourceDTO.setBuildingScheduledTask(buildingScheduledTask);
                                                                                            return Mono.just(islandScheduledJobAndResourceDTO);
                                                                                        });
                                                                            });
                                                                });
                                            }


                                            BuildingScheduledTask buildingScheduledTask = new BuildingScheduledTask(
                                                    allBuildingsServerProperties.getServerId(), islandId, buildingProperties.getIslandBuildingEnum(), initialLvl, nextLvl,
                                                    finalDuration, rawMaterialsAndPopulationCost, userid);

                                            buildingScheduledTask.setLastCalculatedTimestamp(System.currentTimeMillis());

                                            return gatewayWebClientService.assignResources(islandId, rawMaterialsAndPopulationCost)
                                                    .flatMap(islandResourceDTO -> {
                                                        return recordScheduledJob(buildingScheduledTask)
                                                                .flatMap(buildingScheduledTaskReturn -> {
                                                                    IslandScheduledJobAndResourceDTO islandScheduledJobAndResourceDTO = new IslandScheduledJobAndResourceDTO();
                                                                    islandScheduledJobAndResourceDTO.setIslandResourceDTO(islandResourceDTO);
                                                                    islandScheduledJobAndResourceDTO.setBuildingScheduledTask(buildingScheduledTask);
                                                                    return Mono.just(islandScheduledJobAndResourceDTO);
                                                                });
                                                    });
                                        });
                            });
                        }
                        return Mono.error(new Throwable("Building not found"));
                    });
                });
    }

    public Duration multiplyDuration(Duration duration, double multiplier) {
        long nanoseconds = duration.toNanos();

        double multipliedNanos = nanoseconds * multiplier;

        return Duration.ofNanos((long) multipliedNanos);
    }

    public Mono<BuildingScheduledTask> recordScheduledJob(BuildingScheduledTask buildingScheduledTask) {
        return buildingScheduledTaskRepository.save(buildingScheduledTask)
                .flatMap(recordBuildingScheduledTask ->
                        buildingScheduledTaskRepository.findByIslandId(buildingScheduledTask.getIslandId())
                                .collectList()
                                .flatMap(buildingScheduledTasks -> {
                                    if (buildingScheduledTasks.size() < 2) {
                                        try {
                                            buildingScheduledTask.setActualStartTimeStamp(System.currentTimeMillis());
                                            this.rabbitmqService.sendBuildingEndMessage(
                                                    recordBuildingScheduledTask.getId(),
                                                    recordBuildingScheduledTask.getConstructionDuration().toMillis());
                                            return buildingScheduledTaskRepository.save(buildingScheduledTask);
                                        } catch (Exception e) {
                                            return buildingScheduledTaskRepository.delete(recordBuildingScheduledTask)
                                                    .then(Mono.error(new CustomRunTimeException(ExceptionE.ALREADY_EXIST)));
                                        }
                                    }
                                    return Mono.just(recordBuildingScheduledTask);
                                })
                                .onErrorResume(e ->
                                        buildingScheduledTaskRepository.delete(recordBuildingScheduledTask)
                                                .then(Mono.error(new CustomRunTimeException(ExceptionE.ALREADY_EXIST)))
                                )
                );
    }


    public Mono<Void> increaseIslandBuildingLvlDone(String islandId, IslandBuildingEnum islandBuildingEnum, int newLvl) {
        return this.islandBuildingRepository.findById(islandId).flatMap(islandBuilding -> {
            Building building = findBuilding(islandBuilding.getAllBuilding(), islandBuildingEnum);
            assert building != null;
            building.setInitialLvl(newLvl);
            return this.islandBuildingRepository.save(islandBuilding);
        }).switchIfEmpty(Mono.error(Throwable::new)).then();
    }

    public Mono<Void> delete(String islandId) {
        return islandBuildingRepository.deleteById(islandId)
                .doOnError(error -> {
                    throw new RuntimeException("error");
                });
    }

    public Mono<IslandResourceDTO> research(String islandId, SoldierSubTypeEnum soldierSubTypeEnum, Long userId) {
        return islandBuildingRepository.findById(islandId)
                .flatMap(islandBuilding -> {
                    if (!islandBuilding.getUserId().equals(userId)) {
                        return Mono.error(new CustomRunTimeException(ExceptionE.ISLAND_PRIVILEGES));
                    }

                    return serverPropertiesService.get(islandBuilding.getServerId())
                            .flatMap(buildingServerProperties -> {
                                Gunsmith gunsmith = buildingServerProperties.getAllBuildings().getGunsmith();
                                List<SoldierRequirement> soldierRequirementList = gunsmith.getRequirements();
                                if (soldierRequirementList == null || soldierRequirementList.isEmpty()) {
                                    return Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND));
                                }

                                return Flux.fromIterable(soldierRequirementList)
                                        .filter(req -> req.getSoldierType().equals(soldierSubTypeEnum))
                                        .next()
                                        .flatMap(soldierRequirement -> {
                                            boolean allRequirementsMet = soldierRequirement.getBuildingLvlRequirementList().stream()
                                                    .allMatch(buildingLvlRequirement ->
                                                            islandBuilding
                                                                    .getAllBuilding()
                                                                    .findBuildingWithEnum(buildingLvlRequirement.getIslandBuildingEnum())
                                                                    .getInitialLvl() >= buildingLvlRequirement.getBuildingLvl());

                                            if (!allRequirementsMet) {
                                                return Mono.error(new CustomRunTimeException(ExceptionE.NOT_FOUND));
                                            }

                                            return sendRawMaterialAndPopulationCost(islandBuilding.getId(), soldierRequirement);
                                        });
                            });
                });
    }


    public Mono<IslandResourceDTO> sendRawMaterialAndPopulationCost(String islandId, SoldierRequirement soldierRequirement) {
        return gatewayWebClientService
                .assignResources(islandId, soldierRequirement.getRawMaterialsAndPopulationCost())
                .flatMap(islandResourceDTO -> {
                    return gatewayWebClientService.setSoldierTypeResearched(islandId, soldierRequirement.getSoldierType(), true)
                            .thenReturn(islandResourceDTO);
                });
    }

}
