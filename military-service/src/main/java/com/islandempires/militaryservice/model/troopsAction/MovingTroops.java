package com.islandempires.militaryservice.model.troopsAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.dto.SoldierRatios;
import com.islandempires.militaryservice.enums.MissionStatusEnum;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Duration;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class MovingTroops extends Troops {


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ownerIslandMilitaryId")
    @JsonBackReference
    private IslandMilitary ownerIslandMilitary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "targetToIslandMilitary")
    @JsonBackReference
    private IslandMilitary targetToIslandMilitary;

    private MissionTypeEnum missionType;

    private MissionStatusEnum missionStatus;

    private Duration duration;

    public BigInteger calculateTotalAttackPointOfAllUnits() {
        return militaryUnits.calculateTotalAttackPointOfAllUnits();
    }

    @Override
    public String toString() {
        return "MovingTroops{" +
                "id=" + id +
                ", missionType=" + missionType +
                ", missionStatus=" + missionStatus +
                ", duration=" + duration +
                '}';
    }

}
