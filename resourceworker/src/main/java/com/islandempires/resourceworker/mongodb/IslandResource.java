package com.islandempires.resourceworker.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import java.io.Serializable;

@Data
@Document("IslandResource")
@NoArgsConstructor
@AllArgsConstructor
public class IslandResource implements Serializable {

    @Id
    private String islandid;

    /*
     * Raw materials
     */
    private Double wood;
    private Integer hourlyWoodProduction;

    private Double iron;
    private Integer hourlyIronProduction;

    private Double clay;
    private Integer hourlyClayProduction;

    private Double gold;
    private Integer hourlyGoldProduction;

    private Integer rawMaterialStorageSize;


    /*
     * Food
     */
    private Double meat;
    private Integer hourlyMeatProduction;

    private Double fish;
    private Integer hourlyFishProduction;

    private Double wheat;
    private Integer hourlyWheatProduction;

    private Integer foodStorageSize;


    /*
     * Population
     */
    private Double population;
    private Integer hourlyPopulationGrowth;
    private Integer populationLimit;

    private long lastCalculatedTimestamp;


}