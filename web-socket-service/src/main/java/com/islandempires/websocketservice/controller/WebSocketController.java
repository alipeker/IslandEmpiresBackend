package com.islandempires.websocketservice.controller;

import com.islandempires.websocketservice.client.IslandClient;
import com.islandempires.websocketservice.model.Session;
import com.islandempires.websocketservice.model.WebSocketMessage;
import com.islandempires.websocketservice.model.IslandResource;
import com.islandempires.websocketservice.repository.SessionRepository;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.islandempires.websocketservice.client.MyFeignClient;
import com.islandempires.websocketservice.dto.InitialData;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.TimeUnit;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @Autowired
    private IslandClient islandClient;

    @Autowired
    private MyFeignClient myFeignClient;

    private HashMap<String, WebSocketMessage> sessions = new HashMap<>();

    // sessionId, islandId, token

    @MessageMapping("/data.register")
    public WebSocketMessage handleWebSocketConnectListener(@Payload WebSocketMessage webSocketMessage, SimpMessageHeaderAccessor headerAccessor) {
        if(sessions.get(headerAccessor.getSessionId()) != null) {
            return null;
        }

        Long userId = islandClient.whoAmIClient(webSocketMessage.getJwtToken());
        if(userId != null && sessions.values().stream().noneMatch(session ->
                session.getUserId().equals(userId) && session.getServerId().equals(webSocketMessage.getServerId()))) {
            String sessionId = headerAccessor.getSessionId();
            webSocketMessage.setUserId(userId);
            Session session = new Session();
            session.setSessionId(sessionId);
            session.setUserId(userId);
            session.setJwtToken(webSocketMessage.getJwtToken());
            session.setServerId(webSocketMessage.getServerId());
            sessionRepository.save(session);

            sendInitialData(webSocketMessage.getJwtToken(), userId, webSocketMessage.getServerId());
        }

        return webSocketMessage;
    }

    public void sendInitialData(String jwtToken, Long userId, String serverId) {
        String token = jwtToken.split(" ")[1];

        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicBoolean errorExists = new AtomicBoolean(false);

        CompletableFuture<Void> clanFuture = CompletableFuture.runAsync(() -> {
            retry(3, 1, () -> {
                Object userClan = myFeignClient.getUserClan(userId, serverId);
                messagingTemplate.convertAndSend("/topic/initial/clan/" + token, userClan);
            }, errorExists);
        }, executor);

        CompletableFuture<Void> islandsFuture = clanFuture.thenRunAsync(() -> {
            retry(3, 1, () -> {
                List<Object> userIslands = myFeignClient.getAllUserIslands(serverId, userId);
                userIslands.forEach(island -> {
                    messagingTemplate.convertAndSend("/topic/initial/island/start/" + token, island);
                });
            }, errorExists);
        }, executor);

        CompletableFuture<Void> resourcesFuture = clanFuture.thenRunAsync(() -> {
            retry(3, 1, () -> {
                List<Object> islandResources = myFeignClient.getUserResources(userId, serverId);
                islandResources.forEach(islandResource -> {
                    messagingTemplate.convertAndSend("/topic/initial/resource/start/" + token, islandResource);
                });
            }, errorExists);
        }, executor);

        CompletableFuture<Void> buildingsFuture = clanFuture.thenRunAsync(() -> {
            retry(3, 1, () -> {
                List<Object> islandBuilding = myFeignClient.getUserBuildings(userId, serverId);
                islandBuilding.forEach(islandBuild -> {
                    messagingTemplate.convertAndSend("/topic/initial/building/start/" + token, islandBuild);
                });
            }, errorExists);
        }, executor);

        CompletableFuture<Void> militariesFuture = clanFuture.thenRunAsync(() -> {
            retry(3, 1, () -> {
                List<Object> islandMilitaries = myFeignClient.getIslandMilitaries(userId, serverId);
                islandMilitaries.forEach(islandMilitary -> {
                    messagingTemplate.convertAndSend("/topic/initial/military/start/" + token, islandMilitary);
                });
            }, errorExists);
        }, executor);

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(islandsFuture, resourcesFuture, buildingsFuture, militariesFuture);

        allFutures.thenRun(() -> {
            messagingTemplate.convertAndSend("/topic/initial/end/" + token, errorExists.get());
            executor.shutdown();
        }).exceptionally(ex -> {
            ex.printStackTrace();
            executor.shutdown();
            return null;
        });
    }

    private void retry(int maxAttempts, int delayInMinutes, Runnable action, AtomicBoolean errorExists) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                action.run();
                return; // Başarılı olursa döner
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxAttempts) {
                    errorExists.set(true);
                } else {
                    try {
                        TimeUnit.MINUTES.sleep(delayInMinutes);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        if(headers.getSessionId() == null) {
            return;
        }
        sessionRepository.deleteById(headers.getSessionId());
    }


    @MessageMapping("/topic/resources/")
    public void send(@Payload String jwtToken) {
        System.out.println(jwtToken);
    }



    /**
    @Scheduled(fixedRateString ="5000")
    @Transactional(readOnly = true)
    public void run(){
        List<WebSocketMessage> webSocketMessageList = sessions.values().stream().toList();
        List<IslandResource> islandResourceList = islandResourceRepository.findAllByUserIdAndServerId(webSocketMessageList);

        HashMap<String, List<IslandResource>> islandResourceHashMap = new HashMap<>();
        islandResourceList.forEach(islandResource -> {
            Optional<WebSocketMessage> findWebSocketMessage = webSocketMessageList
                    .stream()
                    .filter(webSocketMessage ->
                            webSocketMessage.getUserId().equals(islandResource.getUserId())).findFirst();
            if(findWebSocketMessage.isEmpty()) {
                return;
            }
            islandResourceHashMap.computeIfAbsent(findWebSocketMessage.get().getJwtToken(), k -> new ArrayList<>());
            islandResourceHashMap.get(findWebSocketMessage.get().getJwtToken()).add(islandResource);
        });

        islandResourceHashMap.keySet().forEach(jwtToken -> {
            List<IslandResource> islandResources = islandResourceHashMap.get(jwtToken);
            messagingTemplate.convertAndSend("/topic/resources/" + jwtToken.split(" ")[1], islandResources);
        });
    }**/

    public void sendBuildingDone(String serverId, Long userId, String islandId, Object islandBuilding) {
        sessionRepository.findByServerIdAndUserId(serverId, userId).forEach(session -> {
            messagingTemplate.convertAndSend("/topic/building/" + session.getJwtToken().split(" ")[1], islandBuilding);
        });
    }

    public void sendIslandMilitaryChange(String serverId, Long userId, Object islandMilitary) {
        sessionRepository.findByServerIdAndUserId(serverId, userId).forEach(session -> {
            messagingTemplate.convertAndSend("/topic/military/" + session.getJwtToken().split(" ")[1], islandMilitary);
        });
    }

    public void sendClanChange(Long clanId, Object clan) {
        messagingTemplate.convertAndSend("/topic/clan/" + clanId, clan);
    }

    public void sendClansChange(HashMap<Long, Object> clans) {
        clans.keySet().forEach(clanId -> {
            messagingTemplate.convertAndSend("/topic/clan/" + clanId, clans.get(clanId));
        });
    }

    public void sendClanUserChange(String serverId, Long userId, Object islandClanUser) {
        sessionRepository.findByServerIdAndUserId(serverId, userId).forEach(session -> {
            messagingTemplate.convertAndSend("/topic/clanUser/" + session.getJwtToken().split(" ")[1], islandClanUser);
        });
    }

    public void sendDeleteClanEvent(Long clanId) {
        messagingTemplate.convertAndSend("/topic/clanDelete/" + clanId, new Object());
    }

}
