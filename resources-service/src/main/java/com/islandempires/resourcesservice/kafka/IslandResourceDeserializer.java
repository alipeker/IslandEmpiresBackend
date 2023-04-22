package com.islandempires.resourcesservice.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.resourcesservice.model.IslandResource;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IslandResourceDeserializer implements Deserializer<IslandResource> {

    @Override
    public IslandResource deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, IslandResource.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing IslandResource object", e);
        }
    }

    public List<IslandResource> deserialize(byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<List<IslandResource>> typeRef = new TypeReference<List<IslandResource>>() {};
            return objectMapper.readValue(data, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing IslandResource list", e);
        }
    }
}