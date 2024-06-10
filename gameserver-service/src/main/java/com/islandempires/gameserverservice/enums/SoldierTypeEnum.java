package com.islandempires.gameserverservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SoldierTypeEnum {
    INFANTRYMAN,
    RIFLE,
    CANNON,
    SHIP
}
