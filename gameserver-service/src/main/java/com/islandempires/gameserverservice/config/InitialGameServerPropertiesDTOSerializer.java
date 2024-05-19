package com.islandempires.gameserverservice.config;

import com.islandempires.gameserverservice.dto.initial.InitialGameServerPropertiesDTO;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

public class InitialGameServerPropertiesDTOSerializer implements RedisSerializer<InitialGameServerPropertiesDTO> {

    @Override
    public byte[] serialize(InitialGameServerPropertiesDTO gameServer) throws SerializationException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
             oos.writeObject(gameServer);
             return bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error serializing GameServer", e);
        }
    }

    @Override
    public InitialGameServerPropertiesDTO deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (InitialGameServerPropertiesDTO) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Error deserializing GameServer", e);
        }
    }
}
