package com.islandempires.gameserverservice.dto.island;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IslandResourceDTO implements Serializable {
    private static final long serialVersionUID = 2562090437133615330L;

    private Integer wood;

    private Integer woodHourlyProduction;


    private Integer iron;

    private Integer ironHourlyProduction;
    private Integer ironHourlyProductionMultiply = 0;


    private Integer clay;

    private Integer clayHourlyProduction;
    private Integer clayHourlyProductionMultiply = 0;


    private Integer rawMaterialStorageSize;


    private Double meatFoodCoefficient;

    private Integer meatHourlyProduction;


    private Double fishFoodCoefficient;

    private Integer fishHourlyProduction;

    private Double wheatFoodCoefficient;

    private Integer wheatHourlyProduction;


    private Integer population;

    private Integer populationLimit;


    private Double happinessScore;

    private Double additionalHappinessScore;

}
