package com.islandempires.buildingworker.scheduled;


import com.islandempires.buildingworker.dto.IncreaseOrDecreaseIslandResourceFieldDTO;
import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.enums.IslandResourceEnum;
import com.islandempires.buildingworker.model.scheduled.BuildingScheduledTaskDone;
import com.islandempires.buildingworker.repository.BuildingScheduledTaskRepository;
import com.islandempires.buildingworker.repository.AllBuildingsServerRepository;
import com.islandempires.buildingworker.shared.building.AllBuildingsServerProperties;
import com.islandempires.buildingworker.shared.buildingtype.BaseStructures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.islandempires.buildingworker.util.FindClassField.findBuildingProperty;
import static com.islandempires.buildingworker.util.StaticVariables.getGatewayUrl;
import static com.islandempires.buildingworker.util.StaticVariables.getServiceToken;

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
        this.mongoTemplate.findAll(BuildingScheduledTaskDone.class, "BuildingScheduledTaskDone").forEach(buildingScheduledTask -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", getServiceToken());

                HttpEntity<IslandBuildingEnum> requestEntity = new HttpEntity<>(buildingScheduledTask.getIslandBuildingEnum(), headers);

                restTemplate.exchange(getGatewayUrl() + "/building/increaseIslandBuildingLvlDone/" + buildingScheduledTask.getIslandId() + "/" + buildingScheduledTask.getNextLvl(),
                        HttpMethod.PATCH, requestEntity, Void.class);

                findBuildingClassAndExecuteLevelUpMethod(buildingScheduledTask);

                mongoTemplate.remove(buildingScheduledTask,"BuildingScheduledTaskDone");
            } catch (Exception e) {
                increaseIslandBuildingLvlDoneRollback(buildingScheduledTask);
            }

        });
    }

    public void increaseIslandBuildingLvlDoneRollback(BuildingScheduledTaskDone buildingScheduledTask) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", getServiceToken());

        HttpEntity<IslandBuildingEnum> requestEntity = new HttpEntity<>(buildingScheduledTask.getIslandBuildingEnum(), headers);

        restTemplate.exchange(getGatewayUrl() + "/building/increaseIslandBuildingLvlDoneRollback/" + buildingScheduledTask.getIslandId() + "/" + buildingScheduledTask.getInitialLvl(),
                HttpMethod.PATCH, requestEntity, Void.class);
    }

    public void findBuildingClassAndExecuteLevelUpMethod(BuildingScheduledTaskDone buildingScheduledTask) {
        AllBuildingsServerProperties allBuildingsServerProperties = allBuildingsServerRepository.findById(buildingScheduledTask.getServerId()).get();

        BaseStructures baseStructures = findBuildingProperty(allBuildingsServerProperties, buildingScheduledTask.getIslandBuildingEnum());
        if(baseStructures.getBuildingLevelList().size() > 0) {
            baseStructures.islandBuildingLevelUpExecution(buildingScheduledTask.getNextLvl(), buildingScheduledTask.getIslandId());
        }

    }

}
