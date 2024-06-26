package com.islandempires.militaryservice.model.troopsAction;

import com.islandempires.militaryservice.model.GameServerSoldier;
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

    public void initialize(GameServerSoldier gameServerSoldier) {
        militaryUnits = new MilitaryUnits();
        militaryUnits.initialize(gameServerSoldier);
    }

}
