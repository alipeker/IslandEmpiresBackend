package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.MilitaryUnits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttackerDefenderCasualties {
    private MilitaryUnits attackerMilitaryUnits;

    private MilitaryUnits defenderMilitaryUnits;
}
