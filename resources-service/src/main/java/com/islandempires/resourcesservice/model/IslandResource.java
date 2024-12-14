package com.islandempires.resourcesservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.islandempires.resourcesservice.dto.RawMaterialsDTO;
import com.islandempires.resourcesservice.dto.initial.IslandResourceDTO;
import com.islandempires.resourcesservice.dto.request.ResourceAllocationRequestDTO;
import com.islandempires.resourcesservice.exception.CustomRunTimeException;
import com.islandempires.resourcesservice.exception.ExceptionE;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    private String islandId;

    private Long userId;

    private String serverId;

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
    private Integer ironHourlyProductionMultiply = 0;

    @NotBlank
    private Double clay;
    @NotBlank
    private Integer clayHourlyProduction;
    @NotBlank
    private Integer clayHourlyProductionMultiply = 0;

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

    private long shipCapacity;

    private int shipNumber;

    private int usageShipNumber;

    private long tradingTimeReductionPercentage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdDate;


    public Boolean checkResourceAllocation(ResourceAllocationRequestDTO resourceAllocationRequestDTO) {
        return getWood() >= resourceAllocationRequestDTO.getWood() &&
                getClay() >= resourceAllocationRequestDTO.getClay() &&
                getIron() >= resourceAllocationRequestDTO.getIron() &&
                getPopulationLimit() >= getPopulation() + resourceAllocationRequestDTO.getPopulation();
    }

    public Boolean checkRawMaterialsAllocation(RawMaterialsDTO allocationRawMaterialsDTO) {
        return getWood() >= allocationRawMaterialsDTO.getWood() &&
                getClay() >= allocationRawMaterialsDTO.getClay() &&
                getIron() >= allocationRawMaterialsDTO.getIron();
    }

    public int diminishingResource(RawMaterialsDTO allocationRawMaterialsDTO) {
        int necessaryShipNumber = (int) Math.ceil((double) allocationRawMaterialsDTO.getTotalMaterial() / shipCapacity);

        if(necessaryShipNumber > shipNumber - usageShipNumber ) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_SHIP_NUMBER);
        }

        if(allocationRawMaterialsDTO.getWood() > getWood() ||
                allocationRawMaterialsDTO.getClay() > getClay() ||
                allocationRawMaterialsDTO.getIron() > getIron()) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_RESOURCES);
        }
        increaseUsageShipNumber(necessaryShipNumber);
        this.wood = getWood() - allocationRawMaterialsDTO.getWood();
        this.clay = getClay() - allocationRawMaterialsDTO.getClay();
        this.iron = getIron() - allocationRawMaterialsDTO.getIron();
        return necessaryShipNumber;
    }

    public void increaseUsageShipNumber(int newShipNumber) {
        if(usageShipNumber + newShipNumber > shipNumber) {
            throw new CustomRunTimeException(ExceptionE.INSUFFICIENT_SHIP_NUMBER);
        }
        usageShipNumber += newShipNumber;
    }

    public void addResource(RawMaterialsDTO allocationRawMaterialsDTO) {
        Double newWood = getWood() + allocationRawMaterialsDTO.getWood() > getRawMaterialStorageSize()
                ? getRawMaterialStorageSize() :
                getWood() + allocationRawMaterialsDTO.getWood();

        Double newClay = getClay() + allocationRawMaterialsDTO.getClay() > getRawMaterialStorageSize()
                ? getRawMaterialStorageSize() :
                getClay() + allocationRawMaterialsDTO.getClay();

        Double newIron = getIron() + allocationRawMaterialsDTO.getIron() > getRawMaterialStorageSize()
                ? getRawMaterialStorageSize() :
                getIron() + allocationRawMaterialsDTO.getIron();

        this.wood = newWood;
        this.clay = newClay;
        this.iron = newIron;

    }

    public void addShips(int newShipNumber) {
        usageShipNumber -= newShipNumber;
    }

}