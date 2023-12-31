package com.islandempires.resourcesservice.model;

import com.islandempires.resourcesservice.dto.PopulationDTO;
import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.request.IsResourcesEnoughControl;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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


    public Boolean isResourcesEnoughControl(IsResourcesEnoughControl isResourcesEnoughControl) {
        RawMaterialsDTO rawMaterialsDTO = isResourcesEnoughControl.getRawMaterialsDTO();
        PopulationDTO populationDTO = isResourcesEnoughControl.getPopulationDTO();
        return  getWood() != null && rawMaterialsDTO.getWood() != null && getWood() >= rawMaterialsDTO.getWood() &&
                getClay() != null && rawMaterialsDTO.getClay() != null && getClay() >= rawMaterialsDTO.getClay() &&
                getIron() != null && rawMaterialsDTO.getIron() != null && getIron() >= rawMaterialsDTO.getIron() &&
                getGold() != null && rawMaterialsDTO.getGold() != null && getGold() >= rawMaterialsDTO.getGold() &&
                getPopulation() != null && populationDTO.getPopulation() != null && getPopulation() >= populationDTO.getPopulation()
                ? true: false;
    }

}