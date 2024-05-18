package com.islandempires.sessiontrackingservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.sessiontrackingservice.service.IslandRecordService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IslandConsumerService {
    private final IslandRecordService islandRecordService;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.island-request-topic']}")
    public void getIslandRequestEvent(String islandId) throws JsonProcessingException {
        try {
            islandRecordService.saveIslandRecord(islandId);
        } catch (Exception e) {
        }
    }

}
