package com.islandempires.militaryservice.model.soldier;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import com.islandempires.militaryservice.enums.SoldierTypeEnum;
import com.islandempires.militaryservice.model.GameServerSoldier;
import com.islandempires.militaryservice.model.resource.RawMaterialsAndPopulationCost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"soldierSubTypeName", "gameServerId"})
})
public class SoldierBaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SoldierBaseInfo_generator")
    @SequenceGenerator(name="SoldierBaseInfo_generator", sequenceName = "SoldierBaseInfo_sequence", allocationSize=1)
    protected Long id;

    private String soldierSubTypeName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameServerId")
    @JsonBackReference
    private GameServerSoldier gameServerSoldier;

    private int attackPoint;

    private Duration productionDuration;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(name = "defense_points_mapping",
            joinColumns = @JoinColumn(name = "defensePoints_id"))
    @Column(name = "defense_points")
    private Map<SoldierTypeEnum, Integer> defensePoints;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private RawMaterialsAndPopulationCost rawMaterialsAndPopulationCost;

    public SoldierBaseInfo(String soldierSubTypeName, GameServerSoldier gameServerSoldier) {
        this.soldierSubTypeName = soldierSubTypeName;
        this.gameServerSoldier = gameServerSoldier;
    }
    public SoldierBaseInfo(Long id) {
        this.id = id;
    }

    public void updateRawMaterialAndPopulationCost(RawMaterialsAndPopulationCost rawMaterialsAndPopulationCostUpdate) {
        if(rawMaterialsAndPopulationCost != null) {
            rawMaterialsAndPopulationCost.setPopulation(rawMaterialsAndPopulationCostUpdate.getPopulation());
            rawMaterialsAndPopulationCost.setClay(rawMaterialsAndPopulationCostUpdate.getClay());
            rawMaterialsAndPopulationCost.setWood(rawMaterialsAndPopulationCostUpdate.getWood());
            rawMaterialsAndPopulationCost.setIron(rawMaterialsAndPopulationCostUpdate.getIron());
        } else {
            RawMaterialsAndPopulationCost newRawMaterialsAndPopulationCost = new RawMaterialsAndPopulationCost();
            newRawMaterialsAndPopulationCost.setPopulation(rawMaterialsAndPopulationCostUpdate.getPopulation());
            newRawMaterialsAndPopulationCost.setClay(rawMaterialsAndPopulationCostUpdate.getClay());
            newRawMaterialsAndPopulationCost.setWood(rawMaterialsAndPopulationCostUpdate.getWood());
            newRawMaterialsAndPopulationCost.setIron(rawMaterialsAndPopulationCostUpdate.getIron());
            this.rawMaterialsAndPopulationCost = newRawMaterialsAndPopulationCost;
        }

    }

    @Override
    public String toString() {
        return "SoldierBaseInfo{" +
                "id=" + id +
                ", gameServerSoldier=" + gameServerSoldier +
                ", attackPoint=" + attackPoint +
                ", defensePoints=" + defensePoints +
                ", rawMaterialsAndPopulationCost=" + rawMaterialsAndPopulationCost +
                '}';
    }
}
