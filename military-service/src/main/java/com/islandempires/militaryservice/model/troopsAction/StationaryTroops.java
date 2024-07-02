package com.islandempires.militaryservice.model.troopsAction;

import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.IslandMilitary;
import com.islandempires.militaryservice.model.MilitaryUnits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Entity
//@JsonSerialize(using = CustomStationaryTroopsSerializer.class)
public class StationaryTroops extends Troops {

    public void initialize(GameServerSoldier gameServerSoldier, IslandMilitary islandMilitary) {
        militaryUnits = new MilitaryUnits();
        militaryUnits.initialize(gameServerSoldier);
        militaryUnits.setOwner(islandMilitary);
    }

    public void addReturningSoldiers(MilitaryUnits militaryUnit) {
        militaryUnits.addMilitaryUnitsCount(militaryUnit);
    }

}
