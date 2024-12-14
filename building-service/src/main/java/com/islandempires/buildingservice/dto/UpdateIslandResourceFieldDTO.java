package com.islandempires.buildingservice.dto;

import com.islandempires.buildingservice.enums.IslandResourceEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateIslandResourceFieldDTO implements Serializable {
    @NotNull
    private IslandResourceEnum islandResourceEnum;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Number value;
}

