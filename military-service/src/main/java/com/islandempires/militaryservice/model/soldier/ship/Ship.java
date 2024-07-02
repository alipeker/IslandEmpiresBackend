package com.islandempires.militaryservice.model.soldier.ship;

import com.islandempires.militaryservice.model.soldier.ShipBaseInfo;
import com.islandempires.militaryservice.model.soldier.Soldier;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Ship extends Soldier {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipBaseInfo_id")
    protected ShipBaseInfo shipBaseInfo;
}
