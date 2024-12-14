package com.islandempires.clanservice.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.islandempires.clanservice.repository.ServerUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaOutboxConsumerService {
    private final ServerUserRepository serverUserRepository;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.server-user-delete-topic']}")
    public void deleteIsland(String serverUserEvent) throws JsonProcessingException {
        try {
            String[] serverUserEventSplit = serverUserEvent.split("/");
            String serverId;
            Long userId;
            if(serverUserEventSplit.length == 2) {
                serverId = serverUserEventSplit[0];
                userId = Long.valueOf(serverUserEventSplit[1]);

                serverUserRepository.deleteByServerIdAndUserId(serverId, userId);
            }
        } catch (Exception e) {
        }
    }

}
