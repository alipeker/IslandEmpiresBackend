package com.islandempires.militaryservice.model.war;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.enums.MissionTypeEnum;
import com.islandempires.militaryservice.enums.WarWinnerEnum;
import com.islandempires.militaryservice.model.IslandMilitary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public abstract class WarReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WarReport_generator")
    @SequenceGenerator(name="WarReport_generator", sequenceName = "WarReport_sequence", allocationSize=1)
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attacker_island_military_Id", insertable = false, updatable = false)
    @JsonBackReference
    private IslandMilitary attackerIslandMilitary;

    private String attacker_island_military_Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defender_island_military_id", insertable = false, updatable = false)
    @JsonBackReference
    private IslandMilitary defenderIslandMilitary;

    private String defender_island_military_id;

    private MissionTypeEnum missionTypeEnum;

    private WarWinnerEnum winner;

    private LocalDateTime localDateTime;
}
