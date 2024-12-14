package com.islandempires.militaryservice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.*;

public class SoldierTypeResearchedConverter implements AttributeConverter<Map<SoldierSubTypeEnum, Boolean>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<SoldierSubTypeEnum, Boolean> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to convert map to JSON string.", e);
        }
    }

    @Override
    public Map<SoldierSubTypeEnum, Boolean> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructMapType(HashMap.class, SoldierSubTypeEnum.class, Boolean.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to convert JSON string to map.", e);
        }
    }
}
