package com.islandempires.buildingworker.scheduled;


import com.islandempires.buildingworker.repository.BuildingScheduledTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BuildingScheduledJob {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BuildingScheduledTaskRepository buildingScheduledTaskRepository;

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

        buildingScheduledTaskRepository.findAll().forEach(buildingScheduledTaskDone -> {
            System.out.println();
        });

        /*
        this.mongoTemplate.findAll(BuildingScheduledTask.class, "BuildingScheduledTaskDone").forEach(buildingScheduledTask -> {
            System.out.println(mongoTemplate.findById(buildingScheduledTask.getId(), BuildingScheduledTask.class));
        });*/
    }

}
