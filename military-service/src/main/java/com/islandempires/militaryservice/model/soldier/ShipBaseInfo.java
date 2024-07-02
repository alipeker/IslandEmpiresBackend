package com.islandempires.militaryservice.model.soldier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.model.GameServerSoldier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"shipSubTypeName", "gameServerId"})
})
public class ShipBaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ShipBaseInfo_generator")
    @SequenceGenerator(name="ShipBaseInfo_generator", sequenceName = "ShipBaseInfo_sequence", allocationSize=1)
    protected Long id;

    private String shipSubTypeName;

    private int soldierCapacityOfShip;

    private int canonCapacityOfShip;

    private int totalLootCapacity;

    private Duration timeToTraverseMapCell;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameServerId")
    @JsonBackReference
    private GameServerSoldier gameServerSoldier;
}
