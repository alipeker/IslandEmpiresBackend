package com.islandempires.buildingworker.scheduled;


import com.islandempires.buildingworker.model.scheduled.BuildingScheduledTaskDone;
import com.islandempires.buildingworker.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingworker.repository.AllBuildingsServerRepository;
import com.islandempires.buildingworker.shared.building.AllBuildingsServerProperties;
import com.islandempires.buildingworker.shared.buildingtype.BaseStructures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.islandempires.buildingworker.util.FindClassField.findBuildingProperty;

@Service
public class BuildingScheduledJob {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BuildingScheduledTaskRepository buildingScheduledTaskRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AllBuildingsServerRepository allBuildingsServerRepository;


    @Scheduled(fixedRateString = "10000")
    private void findComplatedScheduledJobs() {
        /*
        this.mongoTemplate.getCollection("BuildingScheduledTaskDone").find().forEach(buildingScheduledTask -> {
                    IslandBuildingEnum islandBuildingEnum = (IslandBuildingEnum) buildingScheduledTask.get("islandBuildingEnum");
                    if(islandBuildingEnum.equals(IslandBuildingEnum.TIMBER_CAMP1)) {
                        TimberCampLevel timberCampLevel = (TimberCampLevel) buildingScheduledTask.get("nxtLvl");
                    }
                    System.out.println(buildingScheduledTask);
                }
        );*/



        this.mongoTemplate.findAll(BuildingScheduledTaskDone.class, "BuildingScheduledTaskDone").forEach(buildingScheduledTask -> {
            findBuildingClassAndExecuteLevelUpMethod(mongoTemplate.findById(buildingScheduledTask.getId(), BuildingScheduledTaskDone.class));
        });
    }

    public void findBuildingClassAndExecuteLevelUpMethod(BuildingScheduledTaskDone buildingScheduledTask) {
        AllBuildingsServerProperties allBuildingsServerProperties = allBuildingsServerRepository.findById(buildingScheduledTask.getServerId()).get();

        BaseStructures baseStructures = findBuildingProperty(allBuildingsServerProperties, buildingScheduledTask.getIslandBuildingEnum());
        if(baseStructures.getBuildingLevelList().size() > 0) {
            baseStructures.islandBuildingLevelUpExecution(buildingScheduledTask.getNextLvl(), buildingScheduledTask.getIslandId());
        }

    }

}
