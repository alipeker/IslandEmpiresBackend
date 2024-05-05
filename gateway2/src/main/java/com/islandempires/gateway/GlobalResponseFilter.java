package com.islandempires.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GlobalResponseFilter implements GlobalFilter, Ordered {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RouterValidator routerValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        try {
            if(routerValidator.isSecured.test(exchange.getRequest())) {
                Long userid = getUserId(headers.get("Authorization").get(0));
                exchange.getRequest().mutate().headers(httpHeaders -> {
                    if(!httpHeaders.containsKey("userid")) {
                        httpHeaders.add("userid", userid.toString());
                        return;
                    }
                });
            }
        } catch (Exception E) {
            throw new UnauthorizedException("User is not authorized to access this resource.");
        }

        return chain.filter(exchange).then(Mono.fromRunnable(()->{
        }));
    }

    public Long getUserId(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Test", "test");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponseDTO> responseEntity = restTemplate.exchange("http://192.168.0.14:1001/auth/me", HttpMethod.GET, requestEntity, UserResponseDTO.class);

        // Process the response
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody().getId();
        } else {
            System.err.println("Failed to fetch data. Status code: " + responseEntity.getStatusCodeValue());
            throw new RuntimeException();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}