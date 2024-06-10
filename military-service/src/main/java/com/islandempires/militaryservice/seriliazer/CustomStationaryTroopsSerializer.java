package com.islandempires.militaryservice.seriliazer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.islandempires.militaryservice.model.troopsAction.StationaryTroops;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;

public class CustomStationaryTroopsSerializer extends JsonSerializer<StationaryTroops> {
    @Override
    public void serialize(StationaryTroops value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof HibernateProxy) {
            // Hibernate proxy nesnesini atla
            gen.writeNull();
        } else {
            // Proxy değilse, nesneyi normal şekilde serileştir
            serializers.defaultSerializeValue(value, gen);
        }
    }
}