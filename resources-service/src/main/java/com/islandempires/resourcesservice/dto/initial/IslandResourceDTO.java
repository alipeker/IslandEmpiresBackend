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
    @Size(min = 1, message = "islandId must be not empty")
    @NotNull
    private String islandId;

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
    @NotBlank
    private Double meatFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Integer meatHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Double fishFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Integer fishHourlyProduction;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Double wheatFoodCoefficient;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Integer wheatHourlyProduction;


    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer population;

    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotNull
    private Integer populationLimit;

    @Positive(message = "Value must be a positive or zero number")
    @NotBlank
    private Double happinessScore;
    @PositiveOrZero(message = "Value must be a positive or zero number")
    @NotBlank
    private Double additionalHappinessScore;

}
