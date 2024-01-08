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
public class PopulationDTO implements Serializable {
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer population;
}
