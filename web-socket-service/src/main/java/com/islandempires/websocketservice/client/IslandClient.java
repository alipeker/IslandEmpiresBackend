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

    public Long whoAmIClient(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Long> responseEntity = restTemplate.exchange("http://localhost:9000/auth/me", HttpMethod.GET, requestEntity, Long.class);

        return responseEntity.getBody();
    }

    public Object getClan(Long clandId) {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://localhost:9001/clan/private/getClan/" + clandId, HttpMethod.GET, requestEntity, Object.class);

        return responseEntity.getBody();
    }

    
    

}
