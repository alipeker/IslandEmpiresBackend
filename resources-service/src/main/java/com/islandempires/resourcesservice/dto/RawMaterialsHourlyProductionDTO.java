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
public class RawMaterialsHourlyProductionDTO implements Serializable {
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer woodHourlyProduction;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer ironHourlyProduction;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer clayHourlyProduction;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer goldHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer rawMaterialStorageSize;
}
