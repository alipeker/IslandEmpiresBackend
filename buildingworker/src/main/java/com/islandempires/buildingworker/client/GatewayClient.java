package com.islandempires.buildingworker.client;

import com.islandempires.buildingworker.dto.IncreaseOrDecreaseIslandResourceFieldDTO;
import com.islandempires.buildingworker.util.StaticVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.islandempires.buildingworker.util.StaticVariables.getServiceToken;

@Component
public class GatewayClient {

    private final WebClient islandResourceWebClient;

    @Autowired
    public GatewayClient(@Qualifier("gatewayWebClientBuilder") WebClient.Builder webClientBuilder,
                         @Value("${urls.gateway}") String gatewayUrl) {
        System.out.println(gatewayUrl);
        this.islandResourceWebClient = webClientBuilder
                                            .baseUrl(gatewayUrl)
                                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Object increaseOrDecreaseIslandResourceField(String islandId, IncreaseOrDecreaseIslandResourceFieldDTO increaseOrDecreaseIslandResourceFieldDTO) {
        return islandResourceWebClient.patch()
                .uri("/resource/updateIslandResourceField/{islandId}", islandId)
                .header(HttpHeaders.AUTHORIZATION, getServiceToken())
                .bodyValue(increaseOrDecreaseIslandResourceFieldDTO)
                .retrieve()
                .bodyToMono(Object.class)
                .doOnError(e -> Mono.error(e));
    }
}
