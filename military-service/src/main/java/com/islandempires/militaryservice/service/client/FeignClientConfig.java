package com.islandempires.militaryservice.service.client;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.optionals.OptionalDecoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class FeignClientConfig {

    @Bean
    @Primary
    public Decoder feignDecoder() {
        return new OptionalDecoder(new SpringDecoder(() -> new HttpMessageConverters(customConverters())));
    }

    @Bean
    @Primary
    public Encoder feignEncoder() {
        return new SpringEncoder(() -> new HttpMessageConverters(customConverters()));
    }

    private List<HttpMessageConverter<?>> customConverters() {
        return List.of(new MappingJackson2HttpMessageConverter());
    }
}
