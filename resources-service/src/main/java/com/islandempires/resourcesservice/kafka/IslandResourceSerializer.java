package com.islandempires.resourcesservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.resourcesservice.model.IslandResource;
import org.apache.kafka.common.serialization.Serializer;

public class IslandResourceSerializer implements Serializer<IslandResource> {

    @Override
    public byte[] serialize(String topic, IslandResource data) {
        if (data == null) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing IslandResource object", e);
        }
    }
}
