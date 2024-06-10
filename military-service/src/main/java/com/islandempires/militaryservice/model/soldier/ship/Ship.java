package com.islandempires.militaryservice.model.soldier.ship;

import com.islandempires.militaryservice.model.soldier.Soldier;
import jakarta.persistence.*;


@Entity
public class Ship extends Soldier {

    private int soldierCapacityOfShip;

    private int canonCapacityOfShip;
}
