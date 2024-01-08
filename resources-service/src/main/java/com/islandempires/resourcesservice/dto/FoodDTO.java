package com.islandempires.resourcesservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodDTO implements Serializable {
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer meat;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer fish;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer wheat;
}
