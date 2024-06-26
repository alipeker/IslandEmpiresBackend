package com.islandempires.gameserverservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class IslandResource implements Serializable {
    private String islandId;

    @NotBlank
    private Double wood;
    @NotBlank
    private Integer woodHourlyProduction;

    @NotBlank
    private Double iron;
    @NotBlank
    private Integer ironHourlyProduction;
    @NotBlank
    private Integer ironHourlyProductionMultiply = 0;

    @NotBlank
    private Double clay;
    @NotBlank
    private Integer clayHourlyProduction;
    @NotBlank
    private Integer clayHourlyProductionMultiply = 0;


    @NotBlank
    private Integer rawMaterialStorageSize;

    @NotBlank
    private Double meatFoodCoefficient;

    @NotBlank
    private Integer meatHourlyProduction;

    @NotBlank
    private Double fishFoodCoefficient;
    @NotBlank
    private Integer fishHourlyProduction;

    @NotBlank
    private Double wheatFoodCoefficient;

    @NotBlank
    private Integer wheatHourlyProduction;


    @NotBlank
    private Integer population;
    @NotBlank
    private Integer temporaryPopulation;

    @NotBlank
    private Integer populationLimit;

    @NotBlank
    private Double happinessScore;
    @NotBlank
    private Double additionalHappinessScore;

}
