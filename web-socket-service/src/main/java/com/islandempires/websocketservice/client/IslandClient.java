package com.islandempires.websocketservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IslandClient {

    @Autowired
    private RestTemplate restTemplate;

    public Boolean isUserIslandOwner(String token, String islandId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange("http://192.168.0.14:9000/island/isUserIslandOwner/" + islandId, HttpMethod.GET, requestEntity, Boolean.class);

        return true;
    }

}
