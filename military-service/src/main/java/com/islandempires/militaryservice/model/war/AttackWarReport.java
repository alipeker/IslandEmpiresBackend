package com.islandempires.militaryservice.model.war;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttackWarReport extends WarReport {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attackerMilitaryUnitReport_id")
    private MilitaryUnitReport attackerMilitaryUnitReport;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attackerMilitaryUnitCausalitiesReport_id")
    private MilitaryUnitReport attackerMilitaryUnitCausalitiesReport;

    @OneToMany(mappedBy = "defenderMilitaryUnitReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MilitaryUnitReport> defenderMilitaryUnitReport;

    @OneToMany(mappedBy = "defenderMilitaryUnitCausalitiesReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MilitaryUnitReport> defenderMilitaryUnitCausalitiesReport;


    public void attackWin() {
        defenderMilitaryUnitCausalitiesReport = defenderMilitaryUnitReport;
    }
}
