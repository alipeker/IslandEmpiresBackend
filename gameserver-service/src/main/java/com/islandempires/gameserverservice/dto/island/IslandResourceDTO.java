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
    private String id;

    private String islandId;

    private Long userId;


    private Integer wood;

    private Integer woodHourlyProduction;


    private Integer iron;

    private Integer ironHourlyProduction;


    private Integer clay;

    private Integer clayHourlyProduction;

    private Integer gold;

    private Integer rawMaterialStorageSize;


    private Double meatFoodCoefficient;

    private Integer meatHourlyProduction;


    private Double fishFoodCoefficient;

    private Integer fishHourlyProduction;

    private Double wheatFoodCoefficient;

    private Integer wheatHourlyProduction;


    private Integer population;

    private Integer populationLimit;


    private Integer temporaryPopulation;

    private Double happinessScore;

    private Double additionalHappinessScore;

}
