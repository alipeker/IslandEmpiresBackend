package com.islandempires.militaryservice.model.war;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
public class AttackWarReport extends WarReport {

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "attackerMilitaryUnitReport_id")
    private MilitaryUnitReport attackerMilitaryUnitReport;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "attackerMilitaryUnitCausalitiesReport_id")
    private MilitaryUnitReport attackerMilitaryUnitCausalitiesReport;

    @OneToMany(mappedBy = "defenderMilitaryUnitReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MilitaryUnitReport> defenderMilitaryUnitReport;

    @OneToMany(mappedBy = "defenderMilitaryUnitCausalitiesReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MilitaryUnitReport> defenderMilitaryUnitCausalitiesReport;

    public AttackWarReport() {
        this.defenderMilitaryUnitReport = new ArrayList<>();
        this.defenderMilitaryUnitCausalitiesReport = new ArrayList<>();
    }


    public void attackWin(AttackWarReport attackWarReport) {
        defenderMilitaryUnitReport.forEach(defenderMilitaryUnitReport -> {
            try {
                MilitaryUnitReport militaryUnitReport = (MilitaryUnitReport) defenderMilitaryUnitReport.clone();
                militaryUnitReport.setDefenderMilitaryUnitCausalitiesReport(attackWarReport);
                militaryUnitReport.setDefenderMilitaryUnitReport(null);
                defenderMilitaryUnitCausalitiesReport.add(militaryUnitReport);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void defenceWin() {
        attackerMilitaryUnitCausalitiesReport = attackerMilitaryUnitReport;
    }

    @Override
    public String toString() {
        return "AttackWarReport{" +
                "attackerMilitaryUnitReport=" + attackerMilitaryUnitReport.id +
                ", attackerMilitaryUnitCausalitiesReport=" + attackerMilitaryUnitCausalitiesReport.id +
                '}';
    }
}
