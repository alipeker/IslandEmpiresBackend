package com.islandempires.resourcesservice.dto.initial;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandResourceDTO implements Serializable {
    private String id;

    private String islandId;

    private Long userId;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer wood;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer woodHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer iron;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer ironHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer clay;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer clayHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer gold;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer rawMaterialStorageSize;



    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Double meatFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer meatHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Double fishFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer fishHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Double wheatFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer wheatHourlyProduction;


    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer population;

    private Integer populationLimit;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer temporaryPopulation;

    private Double happinessScore;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Double additionalHappinessScore;

}
