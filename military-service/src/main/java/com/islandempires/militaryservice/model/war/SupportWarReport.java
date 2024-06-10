package com.islandempires.militaryservice.model.war;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportWarReport extends WarReport {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supporter_military_unit_report")
    private MilitaryUnitReport supporterMilitaryUnitReport;

}
