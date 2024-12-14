package com.islandempires.buildingservice.repository;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.scheduled.BuildingScheduledTask;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BuildingScheduledTaskRepository extends ReactiveCrudRepository<BuildingScheduledTask, String> {
    Flux<BuildingScheduledTask> findByIslandBuildingEnumAndIslandId(IslandBuildingEnum islandBuildingEnum, String islandId);
    Flux<BuildingScheduledTask> findByIsExceptionFalse();

    Flux<BuildingScheduledTask> findByIslandId(String islandId);

    Mono<BuildingScheduledTask> findFirstByIslandIdAndIsExceptionFalseOrderByStartingDateTimestampAsc(String islandId);

    Flux<BuildingScheduledTask> findByIslandIdAndIslandBuildingEnum(String islandId, IslandBuildingEnum islandBuildingEnum);

    Mono<BuildingScheduledTask> findByIslandIdAndIslandBuildingEnumAndIdNot(String islandId, IslandBuildingEnum islandBuildingEnum, String id);

    Flux<BuildingScheduledTask> findByUserIdAndServerId(String userId, String serverId);
}
