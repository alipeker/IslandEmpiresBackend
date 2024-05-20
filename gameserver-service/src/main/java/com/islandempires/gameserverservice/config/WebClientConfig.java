package com.islandempires.gameserverservice.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(60)) // Yanıt beklemesi
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(60)) // Okuma zaman aşımı
                                .addHandlerLast(new WriteTimeoutHandler(60))))); // Yazma zaman aşımı
    }
}

