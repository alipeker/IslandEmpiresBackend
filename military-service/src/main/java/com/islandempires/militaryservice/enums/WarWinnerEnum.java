package com.islandempires.militaryservice.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum WarWinnerEnum {
    DEFENCE_WIN,
    ATTACK_WIN
}
