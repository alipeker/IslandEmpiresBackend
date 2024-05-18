package com.islandempires.websocketservice.controller;

import com.islandempires.websocketservice.client.IslandClient;
import com.islandempires.websocketservice.model.WebSocketMessage;
import com.islandempires.websocketservice.model.IslandResource;
import com.islandempires.websocketservice.repository.IslandResourceRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Autowired
    private IslandClient islandClient;

    private HashMap<String, String> sessions = new HashMap<>();

    // sessionId, islandId, token

    @MessageMapping("/data.register")
    public WebSocketMessage handleWebSocketConnectListener(@Payload WebSocketMessage webSocketMessage, SimpMessageHeaderAccessor headerAccessor) {
        if(sessions.get(headerAccessor.getSessionId()) != null) {
            return null;
        }

        if(islandClient.isUserIslandOwner(webSocketMessage.getJwtToken(), webSocketMessage.getIslandId())) {
            String sessionId = headerAccessor.getSessionId();
            sessions.put(sessionId, webSocketMessage.getIslandId());
        }

        return webSocketMessage;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        sessions.remove(event.getSessionId());
    }


    @MessageMapping("/topic/resources/")
    public void send(@Payload IslandResource chatMessage) {
        System.out.println(chatMessage.getClay());
    }



    @Scheduled(fixedRateString ="5000")
    public void run(){
        List<IslandResource> islandResourceList = islandResourceRepository.findAllById(sessions.values());

        islandResourceList.forEach(islandResource -> {
            messagingTemplate.convertAndSend("/topic/data/" + islandResource.getId(), islandResource);
        });

    }

}
