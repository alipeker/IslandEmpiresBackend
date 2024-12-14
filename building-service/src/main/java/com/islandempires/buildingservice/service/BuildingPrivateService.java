package com.islandempires.buildingservice.service;


import com.islandempires.buildingservice.dto.IncreaseOrDecreaseIslandResourceFieldDTO;
import com.islandempires.buildingservice.dto.UpdateIslandResourceFieldDTO;
import com.islandempires.buildingservice.dto.UpdateIslandTradingDTO;
import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.enums.IslandResourceEnum;
import com.islandempires.buildingservice.model.IslandBuilding;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.building.Building;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import com.islandempires.buildingservice.rabbitmq.RabbitMqConfig;
import com.islandempires.buildingservice.rabbitmq.RabbitmqService;
import com.islandempires.buildingservice.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingservice.repository.IslandBuildingRepository;
import com.islandempires.buildingservice.service.client.GatewayWebClientService;
import com.islandempires.buildingservice.shared.buildinglevelspec.*;
import com.islandempires.buildingservice.shared.repository.BuildingsServerPropertiesRepository;
import com.islandempires.buildingservice.shared.service.ServerPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.islandempires.buildingservice.util.FindInListWithField.findBuilding;

import reactor.core.publisher.Flux;
import com.islandempires.buildingservice.dto.IslandBuildingAndScheduledJobDTO;

@Service
@RequiredArgsConstructor
public class BuildingPrivateService {

    private final IslandBuildingRepository islandBuildingRepository;

    private final BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    public final GatewayWebClientService gatewayWebClientService;

    private final ServerPropertiesService serverPropertiesService;

    private final BuildingsServerPropertiesRepository buildingsServerPropertiesRepository;

    private final RabbitmqService rabbitmqService;

    private final BuildingService buildingService;

    public Mono<IslandBuilding> get(String islandId) {
        return islandBuildingRepository
                .findById(islandId)
                .flatMap(Mono::just);
    }

    public Flux<IslandBuildingAndScheduledJobDTO> get(Long userId, String serverId) {
    return islandBuildingRepository.findByUserIdAndServerId(userId, serverId)
            .flatMap(islandBuilding -> {
                return buildingScheduledTaskRepository.findByIslandId(islandBuilding.getId())
                        .collectList()
                        .map(buildingScheduledTasks -> {
                            if (!islandBuilding.getUserId().equals(userId)) {
                                throw new RuntimeException("");
                            }
                            IslandBuildingAndScheduledJobDTO dto = new IslandBuildingAndScheduledJobDTO();
                            dto.setIslandBuilding(islandBuilding);
                            List<BuildingScheduledTask> buildingScheduledTaskList = buildingScheduledTasks.stream()
                                    .sorted((task1, task2) -> Long.compare(task1.getStartingDateTimestamp(), task2.getStartingDateTimestamp()))
                                    .toList();
                            dto.setBuildingScheduledTaskList(buildingScheduledTaskList);
                            return dto;
                        });
            });
    }

    public Flux<IslandBuilding> getUserIdAndServerId(Long userId, String serverId) {
        return islandBuildingRepository.findByUserIdAndServerId(userId, serverId);
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

    public String removeFirstAndLast(String str) {
        if (str == null || str.length() < 2) {
            return ""; // Handle empty or short strings
        }

        return str.substring(1, str.length() - 1);
    }

    @RabbitListener(queues = RabbitMqConfig.BUILDING_QUEUE_NAME)
    public Mono<Void> increaseBuildingLvlDone(String buildingJobId) {
        buildingJobId = removeFirstAndLast(buildingJobId);
        if (buildingJobId == null || buildingJobId.equals("")) {
            return Mono.empty();
        }

        return buildingScheduledTaskRepository.findById(buildingJobId)
                .switchIfEmpty(Mono.empty())
                .flatMap(buildingScheduledTask ->
                        processBuildingUpgrade(buildingScheduledTask)
                                .onErrorResume(e -> {
                                    return buildingScheduledTaskRepository.findByIslandIdAndIslandBuildingEnum(buildingScheduledTask.getIslandId(), buildingScheduledTask.getIslandBuildingEnum())
                                            .flatMap(refundScheduledTask -> {
                                                if (refundScheduledTask.getId().equals(buildingScheduledTask.getId())) {
                                                    return refundResourcesWithTime(refundScheduledTask);
                                                }
                                                return refundResources(refundScheduledTask);
                                            }).then();
                                })
                                .then(ifExceptionNonExistDeleteTask(buildingScheduledTask))
                                .then(sendTheNextToRabbitExchange(buildingScheduledTask))
                                .then(buildingService.get(buildingScheduledTask.getIslandId(), buildingScheduledTask.getUserId())
                                        .flatMap(islandBuilding -> {
                                            return gatewayWebClientService.sendWebsocket(buildingScheduledTask.getServerId(),
                                                    buildingScheduledTask.getUserId(),
                                                    buildingScheduledTask.getIslandId(),
                                                    islandBuilding);
                                        })
                                )
                );
    }

    public Mono<Void> refundResourcesWithTime(BuildingScheduledTask buildingScheduledTask) {
        return gatewayWebClientService
                .refundResources(buildingScheduledTask.getIslandId(), buildingScheduledTask.getRawMaterialsAndPopulationCost())
                .onErrorResume(e -> {
                    buildingScheduledTask.setException(true);
                    return buildingScheduledTaskRepository.save(buildingScheduledTask)
                            .onErrorResume(e2 -> Mono.empty())
                            .then(Mono.empty());
                })
                .flatMap(refundResult -> {
                    return buildingScheduledTaskRepository.deleteById(buildingScheduledTask.getId())
                            .then(islandBuildingRepository.findById(buildingScheduledTask.getIslandId())
                                    .flatMap(islandBuilding -> {
                                        Duration duration = islandBuilding.getRefundTime();
                                        if (duration != null) {
                                            islandBuilding.setRefundTime(duration.plus(buildingScheduledTask.getConstructionDuration()));
                                        } else {
                                            islandBuilding.setRefundTime(buildingScheduledTask.getConstructionDuration());
                                        }
                                        return islandBuildingRepository.save(islandBuilding);
                                    })
                            );
                })
                .then(Mono.empty());
    }


    public Mono<Void> refundResources(BuildingScheduledTask buildingScheduledTask) {
        return gatewayWebClientService.refundResources(buildingScheduledTask.getIslandId(), buildingScheduledTask.getRawMaterialsAndPopulationCost())
                .onErrorResume(e -> {
                    buildingScheduledTask.setException(true);
                    return buildingScheduledTaskRepository.save(buildingScheduledTask)
                            .doOnError(Mono::error)
                            .then(Mono.empty());
                })
                .flatMap(islandResourceDTO -> {
                    return buildingScheduledTaskRepository
                            .deleteById(buildingScheduledTask.getId())
                            .then(Mono.empty());
                })
                .then(Mono.empty());
    }

    public Mono<Void> ifExceptionNonExistDeleteTask(BuildingScheduledTask buildingScheduledTask) {
        if (!buildingScheduledTask.getException()) {
            return buildingScheduledTaskRepository
                    .deleteById(buildingScheduledTask.getId())
                    .then(Mono.empty());
        }
        return Mono.empty();
    }

    public Mono<Void> sendTheNextToRabbitExchange(BuildingScheduledTask buildingScheduledTask) {
        if (buildingScheduledTask.getException()) {
            return Mono.empty();
        }
        return buildingScheduledTaskRepository.findFirstByIslandIdAndIsExceptionFalseOrderByStartingDateTimestampAsc(buildingScheduledTask.getIslandId())
                .switchIfEmpty(Mono.empty())
                .flatMap(buildingScheduledTaskNext -> {
                    this.rabbitmqService.sendBuildingEndMessage(
                            buildingScheduledTaskNext.getId(),
                            buildingScheduledTaskNext.getConstructionDuration().toMillis());
                    buildingScheduledTaskNext.setActualStartTimeStamp(System.currentTimeMillis());
                    return buildingScheduledTaskRepository.save(buildingScheduledTaskNext);
                })
                .then(Mono.empty());
    }


    //@Scheduled(fixedRate = 600000)
    public void getScheduledJobs() {
        List<BuildingScheduledTask> buildingScheduledTaskList = buildingScheduledTaskRepository.findByIsExceptionFalse().collectList().share().block();
        assert buildingScheduledTaskList != null;
        for (BuildingScheduledTask buildingScheduledTask : buildingScheduledTaskList) {
            if (buildingScheduledTask.getStartingDateTimestamp() + buildingScheduledTask.getConstructionDuration().toMillis()
                    < new Date().getTime()) {
                increaseBuildingLvlDone(buildingScheduledTask.getId());
            }
        }
        ;
    }

    public Mono<Void> processBuildingUpgrade(BuildingScheduledTask buildingScheduledTask) {

        return serverPropertiesService.get(buildingScheduledTask.getServerId()).flatMap(serverProperties -> {
            IslandBuildingEnum islandBuildingEnum = buildingScheduledTask.getIslandBuildingEnum();
            switch (islandBuildingEnum) {
                case ACADEMIA -> {
                    AcademiaLevel academiaLevel =
                            serverProperties.getAllBuildings().getAcademia().getAcademiaLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateMaxMissionaryCount(buildingScheduledTask.getIslandId(), academiaLevel.getMissionaryNumber())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case BARRACK -> {
                    BarrackLevel barrackLevel =
                            serverProperties.getAllBuildings().getBarrack().getBarrackLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateInfantrymanTimeReduction(buildingScheduledTask.getIslandId(), barrackLevel.getTimeReductionPercentage())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case RIFFLE_BARRACK -> {
                    RiffleBarrackLevel riffleBarrackLevel =
                            serverProperties.getAllBuildings().getRiffleBarrack().getRiffleBarrackLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateRifleTimeReduction(buildingScheduledTask.getIslandId(), riffleBarrackLevel.getTimeReductionPercentage())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case CANNON_CAMP -> {
                    CannonCampLevel cannonCampLevel =
                            serverProperties.getAllBuildings().getCannonCamp().getCannonCampLevels().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateCannonTimeReduction(buildingScheduledTask.getIslandId(), cannonCampLevel.getTimeReductionPercentage())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case GUNSMITH -> {
                    return this.upgradeLvl(buildingScheduledTask);
                }
                case TIMBER_CAMP1 -> {
                    TimberCampLevel timberCampLevel =
                            serverProperties.getAllBuildings().getTimberCamp1().getTimberCampLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyWoodProduction = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyWoodProduction = timberCampLevel.getHourlyWoodProduction();
                    } else {
                        TimberCampLevel timberCampPreviousLevel =
                                serverProperties.getAllBuildings().getTimberCamp1().getTimberCampLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyWoodProduction = timberCampLevel.getHourlyWoodProduction() - timberCampPreviousLevel.getHourlyWoodProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.WOOD_HOURLY_PRODUCTION, hourlyWoodProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case TIMBER_CAMP2 -> {
                    TimberCampLevel timberCamp2Level =
                            serverProperties.getAllBuildings().getTimberCamp2().getTimberCampLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyWoodProduction2 = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyWoodProduction2 = timberCamp2Level.getHourlyWoodProduction();
                    } else {
                        TimberCampLevel timberCamp2PreviousLevel =
                                serverProperties.getAllBuildings().getTimberCamp2().getTimberCampLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyWoodProduction2 = timberCamp2Level.getHourlyWoodProduction() - timberCamp2PreviousLevel.getHourlyWoodProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.WOOD_HOURLY_PRODUCTION, hourlyWoodProduction2))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case CLAY_MINE -> {
                    ClayMineLevel clayMineLevel =
                            serverProperties.getAllBuildings().getClayMine().getClayMineLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyClayProduction = clayMineLevel.getHourlyClayProduction();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.CLAY_HOURLY_PRODUCTION, hourlyClayProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case BRICK_WORKS -> {
                    BricksWorksLevel bricksWorksLevel =
                            serverProperties.getAllBuildings().getBrickWorks().getBricksWorksLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyClayProductionMultiply = bricksWorksLevel.getClayProductionIncreasePercentage();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.CLAY_HOURL_PRODUCTION_MULTIPLY, hourlyClayProductionMultiply))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case IRON_MINE -> {
                    IronMineLevel ironMineLevel =
                            serverProperties.getAllBuildings().getIronMine().getIronMineLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyIronProduction = ironMineLevel.getHourlyIronProduction();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.IRON_HOURLY_PRODUCTION, hourlyIronProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case FOUNDRY -> {
                    FoundryLevel foundryLevel =
                            serverProperties.getAllBuildings().getFoundry().getFoundryLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyIronProductionMultiply = foundryLevel.getIronProductionIncreasePercentage();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.IRON_HOURL_PRODUCTION_MULTIPLY, hourlyIronProductionMultiply))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case HOUSES -> {
                    HouseLevel houseLevel =
                            serverProperties.getAllBuildings().getHouses().getHouseLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    double additionalHappinessScoreHouse = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        additionalHappinessScoreHouse = houseLevel.getAdditionalHappinessScore();
                    } else {
                        HouseLevel housePreviousLevel =
                                serverProperties.getAllBuildings().getHouses().getHouseLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        additionalHappinessScoreHouse = houseLevel.getAdditionalHappinessScore() - housePreviousLevel.getAdditionalHappinessScore();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.ADDITIONAL_HAPPINESS_SCORE, additionalHappinessScoreHouse))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case DAIRY_FARM1 -> {
                    DairyFarmLevel dairyFarmLevel =
                            serverProperties.getAllBuildings().getDairyFarm1().getDairyFarmLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyMeatProduction = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyMeatProduction = dairyFarmLevel.getHourlyMeatProduction();
                    } else {
                        DairyFarmLevel dairyFarmPreviousLevel =
                                serverProperties.getAllBuildings().getDairyFarm1().getDairyFarmLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyMeatProduction = dairyFarmLevel.getHourlyMeatProduction() - dairyFarmPreviousLevel.getHourlyMeatProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.MEAT_HOURLY_PRODUCTION, hourlyMeatProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case DAIRY_FARM2 -> {
                    DairyFarmLevel dairyFarm2Level =
                            serverProperties.getAllBuildings().getDairyFarm2().getDairyFarmLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyMeatProduction2 = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyMeatProduction2 = dairyFarm2Level.getHourlyMeatProduction();
                    } else {
                        DairyFarmLevel dairyFarm2PreviousLevel =
                                serverProperties.getAllBuildings().getDairyFarm1().getDairyFarmLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyMeatProduction2 = dairyFarm2Level.getHourlyMeatProduction() - dairyFarm2PreviousLevel.getHourlyMeatProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.MEAT_HOURLY_PRODUCTION, hourlyMeatProduction2))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case FISHER -> {
                    FisherLevel fisherLevel =
                            serverProperties.getAllBuildings().getFisher().getFisherLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyFisherProduction = fisherLevel.getHourlyFishProduction();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.FISH_HOURLY_PRODUCTION, hourlyFisherProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case MILL1 -> {
                    MillLevel millLevel =
                            serverProperties.getAllBuildings().getMill1().getMillLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyWheatProduction = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyWheatProduction = millLevel.getHourlyWheatProduction();
                    } else {
                        MillLevel millPreviousLevel =
                                serverProperties.getAllBuildings().getMill1().getMillLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyWheatProduction = millLevel.getHourlyWheatProduction() - millPreviousLevel.getHourlyWheatProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.WHEAT_HOURLY_PRODUCTION, hourlyWheatProduction))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case MILL2 -> {
                    MillLevel mill2Level =
                            serverProperties.getAllBuildings().getMill2().getMillLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int hourlyWheatProduction2 = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        hourlyWheatProduction2 = mill2Level.getHourlyWheatProduction();
                    } else {
                        MillLevel mill2PreviousLevel =
                                serverProperties.getAllBuildings().getMill2().getMillLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        hourlyWheatProduction2 = mill2Level.getHourlyWheatProduction() - mill2PreviousLevel.getHourlyWheatProduction();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.WHEAT_HOURLY_PRODUCTION, hourlyWheatProduction2))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case EMBASSY -> {
                    EmbassyLevel embassyLevel =
                            serverProperties.getAllBuildings().getEmbassy().getEmbassyLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    Long userId = buildingScheduledTask.getUserId();
                    String serverId = buildingScheduledTask.getServerId();
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        return gatewayWebClientService.joinClanAllowed(serverId, userId).then(this.upgradeLvl(buildingScheduledTask));
                    } else if (buildingScheduledTask.getNextLvl() == 2) {
                        return gatewayWebClientService.createClanAllowed(serverId, userId, embassyLevel.getNumberOfClanUser()).then(this.upgradeLvl(buildingScheduledTask));
                    } else {
                        return gatewayWebClientService.setClanMaxMemberNumber(serverId, userId, embassyLevel.getNumberOfClanUser()).then(this.upgradeLvl(buildingScheduledTask));
                    }
                }
                case WATCH_TOWER -> {
                    WatchTowerLevel watchTowerLevel =
                            serverProperties.getAllBuildings().getWatchTower().getWatchTowerLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateVisibilityRangeAndObservableCapacity(buildingScheduledTask.getIslandId(),
                                    watchTowerLevel.getVisibilityRange(),
                                    watchTowerLevel.getObservableCapacity().intValue())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case DEFENCE_TOWER -> {
                    DefenceTowerLevel defenceTowerLevel =
                            serverProperties.getAllBuildings().getDefenceTower().getDefenceTowerLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateDefencePointPercentage(buildingScheduledTask.getIslandId(),
                                    defenceTowerLevel.getDefencePointPercentage())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case WAREHOUSE -> {
                    WareHouseLevel wareHouseLevel =
                            serverProperties.getAllBuildings().getWareHouse().getWareHouseLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    int rawMaterialStorageSize = wareHouseLevel.getStorageAmount();
                    return gatewayWebClientService.updateIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new UpdateIslandResourceFieldDTO(IslandResourceEnum.RAW_MATERIAL_STORAGE_SIZE, rawMaterialStorageSize))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case FOOD_WAREHOUSE1 -> {
                    FoodWareHouseLevel foodWareHouseLevel =
                            serverProperties.getAllBuildings().getFoodWareHouse1().getFoodWareHouseLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    double additionalHappinessScore = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        additionalHappinessScore = foodWareHouseLevel.getAdditionalHappinessScore();
                    } else {
                        FoodWareHouseLevel foodWareHousePreviousLevel =
                                serverProperties.getAllBuildings().getFoodWareHouse1().getFoodWareHouseLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        additionalHappinessScore = foodWareHouseLevel.getAdditionalHappinessScore() - foodWareHousePreviousLevel.getAdditionalHappinessScore();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.ADDITIONAL_HAPPINESS_SCORE, additionalHappinessScore))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case FOOD_WAREHOUSE2 -> {
                    FoodWareHouseLevel foodWareHouse2Level =
                            serverProperties.getAllBuildings().getFoodWareHouse2().getFoodWareHouseLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    double additionalHappinessScore2 = 0;
                    if (buildingScheduledTask.getNextLvl() == 1) {
                        additionalHappinessScore2 = foodWareHouse2Level.getAdditionalHappinessScore();
                    } else {
                        FoodWareHouseLevel foodWareHouse2PreviousLevel =
                                serverProperties.getAllBuildings().getFoodWareHouse2().getFoodWareHouseLevelList().get(buildingScheduledTask.getInitialLvl() - 1);
                        additionalHappinessScore2 = foodWareHouse2Level.getAdditionalHappinessScore() - foodWareHouse2PreviousLevel.getAdditionalHappinessScore();
                    }
                    return gatewayWebClientService.increaseOrDecreaseIslandResourceField(buildingScheduledTask.getIslandId(),
                                    new IncreaseOrDecreaseIslandResourceFieldDTO(IslandResourceEnum.ADDITIONAL_HAPPINESS_SCORE, additionalHappinessScore2))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case SHIPYARD -> {
                    ShipyardLevel shipyardLevel =
                            serverProperties.getAllBuildings().getShipyard().getShipyardLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateShipTimeReduction(buildingScheduledTask.getIslandId(), shipyardLevel.getTimeReductionPercentage())
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case ISLAND_HEADQUARTERS -> {
                    IslandHeadquarterLevel islandHeadquarterLevel =
                            serverProperties.getAllBuildings().getIslandHeadquarter().getIslandHeadquarterLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return islandBuildingRepository.findById(buildingScheduledTask.getIslandId())
                            .flatMap(islandBuilding -> {
                                islandBuilding.setTimeReductionPercentage(islandHeadquarterLevel.getTimeReductionPercentage());
                                return islandBuildingRepository.save(islandBuilding).then();
                            })
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                case COMMERCIAL_PORT -> {
                    CommercialPortLevel commercialPortLevel =
                            serverProperties.getAllBuildings().getCommercialPort().getCommercialPortLevelList().get(buildingScheduledTask.getNextLvl() - 1);
                    return gatewayWebClientService
                            .updateIslandTrading(buildingScheduledTask.getIslandId(), new UpdateIslandTradingDTO(commercialPortLevel.getCargoShipNumber(),
                                    commercialPortLevel.getCargoShipCapacity(), commercialPortLevel.getTimeReductionPercentage()))
                            .then(this.upgradeLvl(buildingScheduledTask));
                }
                default -> {
                }
            }
            return null;
        });
    }

    public Mono<Void> upgradeLvl(BuildingScheduledTask buildingScheduledTask) {
        return islandBuildingRepository.findById(buildingScheduledTask.getIslandId())
                .flatMap(islandBuilding -> {
                    Building building = islandBuilding.getAllBuilding().findBuildingWithEnum(buildingScheduledTask.getIslandBuildingEnum());
                    building.setInitialLvl(buildingScheduledTask.getNextLvl());
                    return islandBuildingRepository.save(islandBuilding).then();
                });
    }

}
