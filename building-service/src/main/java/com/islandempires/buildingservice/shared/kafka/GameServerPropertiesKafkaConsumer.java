package com.islandempires.buildingservice.shared.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.buildingservice.shared.service.ServerPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class GameServerPropertiesKafkaConsumer {

    @Autowired
    private ServerPropertiesService serverPropertiesService;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "game-server-properties-change-topic")
    public void GameServerPropertiesChangeConsumer() throws JsonProcessingException {
        serverPropertiesService.getAllBuildingsServerProperties();
    }

}
