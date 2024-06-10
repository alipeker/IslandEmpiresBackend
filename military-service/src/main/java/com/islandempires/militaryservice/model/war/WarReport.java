package com.islandempires.militaryservice.model.war;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "attacker_island_military_Id")
    @JsonBackReference
    private IslandMilitary attackerIslandMilitary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defender_island_military_id")
    @JsonBackReference
    private IslandMilitary defenderIslandMilitary;

    private LocalDateTime localDateTime;
}
