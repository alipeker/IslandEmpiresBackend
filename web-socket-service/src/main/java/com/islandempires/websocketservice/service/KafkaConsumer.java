package com.islandempires.websocketservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.websocketservice.dto.ClanUserMessageDTO;
import com.islandempires.websocketservice.dto.SessionDTO;
import com.islandempires.websocketservice.dto.ClanMessageDTO;
import com.islandempires.websocketservice.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.islandempires.websocketservice.client.IslandClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IslandClient islandClient;

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.resource-sender-topic']}")
    public void resourceData(String sessionDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SessionDTO sessionData = objectMapper.readValue(sessionDTO, SessionDTO.class);
            messagingTemplate.convertAndSend("/topic/resources/" + sessionData.getJwtToken().split(" ")[1], sessionData.getIslandResourceList());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.clan-stream']}")
    public void clanConsumer(String message) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClanMessageDTO clanMessageDTO = objectMapper.readValue(message, ClanMessageDTO.class);
            messagingTemplate.convertAndSend("/topic/clan/" + clanMessageDTO.getId(), clanMessageDTO.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(groupId = "#{@environment['spring.kafka.consumer.group-id']}", topics = "#{@environment['spring.kafka.consumer.clan-user-change-topic']}")
    public void clanUserConsumer(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClanUserMessageDTO clanMessageDTO = objectMapper.readValue(message, ClanUserMessageDTO.class);
            sessionRepository.findByServerIdAndUserId(clanMessageDTO.getServerId(), clanMessageDTO.getId())
                    .forEach(session -> {
                        messagingTemplate.convertAndSend("/topic/clanUser/" + session.getJwtToken().split(" ")[1], clanMessageDTO.getData());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
