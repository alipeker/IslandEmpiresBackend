package com.islandempires.resourcesservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("IslandResource")
@AllArgsConstructor
@NoArgsConstructor
public class IslandResource implements Serializable {
    @Id
    private String islandid;

    /*
    * Raw materials
    */
    private Double wood;
    private Double hourlyWoodProduction;

    private Double iron;
    private Double hourlyIronProduction;

    private Double clay;
    private Double hourlyClayProduction;

    private Double gold;
    private Double hourlyGoldProduction;

    private Double rawMaterialStorage;


    /*
     * Food
    */
    private Double meat;
    private Double hourlyMeatProduction;

    private Double fish;
    private Double hourlyFishProduction;

    private Double wheat;
    private Double hourlyWheatProduction;

    private Double foodStorage;


    /*
     * Population
    */
    private Double population;
    private Double hourlyPopulationGrowth;
    private Long populationLimit;


    private Long lastCalculatedTimestamp;
}