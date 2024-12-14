package com.islandempires.resourcesservice.model;

import com.islandempires.resourcesservice.enums.TransportResourceStatusEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTransportState implements Serializable {
    @Enumerated(EnumType.STRING)
    private TransportResourceStatusEnum transportResourceStatusEnum;

    private LocalDateTime localDateTime;

    private Duration duration;


    public ResourceTransportState(TransportResourceStatusEnum transportResourceStatusEnum, Duration duration) {
        this.transportResourceStatusEnum = transportResourceStatusEnum;
        this.duration = duration;
        this.localDateTime = LocalDateTime.now();
    }
}
