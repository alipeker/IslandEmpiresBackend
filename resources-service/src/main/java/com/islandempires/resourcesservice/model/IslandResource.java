package com.islandempires.resourcesservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document("IslandResource")
@NoArgsConstructor
@AllArgsConstructor
public class IslandResource implements Serializable {
    @Id
    @NotBlank
    private String id;

    @NotBlank
    private String islandId;

    private Long userId;

    /*
    * Raw materials
    */
    @NotBlank
    private Double wood;
    @NotBlank
    private Integer woodHourlyProduction;

    @NotBlank
    private Double iron;
    @NotBlank
    private Integer ironHourlyProduction;

    @NotBlank
    private Double clay;
    @NotBlank
    private Integer clayHourlyProduction;

    @NotBlank
    private Double gold;

    @NotBlank
    private Integer rawMaterialStorageSize;


    /*
     * Food
    */
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

 
    /*
     * Population
    */
    @NotBlank
    private Integer population;
    @NotBlank
    private Integer temporaryPopulation;

    /*
    * meatFoodCoefficient
    * fishHourlyProduction
    * wheatHourlyProduction
    * (1 * 1 + 2 * 1 + 3 * 1)
    * 6
    * */
    @NotBlank
    private Integer populationLimit;

    /*
     * population + temporaryPopulation <= populationLimit
     * => happinessScore = additionalHappinessScore + 1
     * not
     * => happinessScore = additionalHappinessScore + populationLimit / (population + temporaryPopulation)
     * */

    @NotBlank
    private Double happinessScore;
    @NotBlank
    private Double additionalHappinessScore;

    @NotBlank
    private long lastCalculatedTimestamp;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdDate;

}