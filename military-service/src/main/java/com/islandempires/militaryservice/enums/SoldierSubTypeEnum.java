package com.islandempires.militaryservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SoldierSubTypeEnum {
    PIKEMAN, AXEMAN, ARCHER, SWORDSMAN,
    LIGHT_ARMED_MUSKETEER, MEDIUM_ARMED_MUSKETEER, HEAVY_ARMED_MUSKETEER,
    CULVERIN, MORTAR, RIBAULT,
    HOLK, GUN_HOLK, CARRACK
}
