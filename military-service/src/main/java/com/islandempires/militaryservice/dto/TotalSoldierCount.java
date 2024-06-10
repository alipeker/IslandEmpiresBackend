package com.islandempires.militaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalSoldierCount {
    private BigInteger infantrymanCount;

    private BigInteger rifleCount;

    private BigInteger cannonCount;

    private BigInteger shipCount;

    public void addInfantrymanCount(BigInteger infantrymanCount) {
        this.infantrymanCount = this.infantrymanCount.add(infantrymanCount);
    }

    public void addRifleCount(BigInteger rifleCount) {
        this.rifleCount = this.infantrymanCount.add(rifleCount);
    }

    public void addCannonCount(BigInteger cannonCount) {
        this.cannonCount = this.infantrymanCount.add(cannonCount);
    }

    public void addShipCount(BigInteger shipCount) {
        this.shipCount = this.infantrymanCount.add(shipCount);
    }

    public BigInteger getTotalSoldierCount() {
        return this.infantrymanCount.add(this.rifleCount).add(this.cannonCount).add(this.shipCount);
    }

    public void addSoldierCount(TotalSoldierCount totalSoldierCount) {
        this.addInfantrymanCount(totalSoldierCount.getInfantrymanCount());
        this.addRifleCount(totalSoldierCount.getRifleCount());
        this.addCannonCount(totalSoldierCount.getCannonCount());
        this.addShipCount(totalSoldierCount.getShipCount());
    }

    public SoldierRatios calculateSoldierRatio() {
        if(getTotalSoldierCount().compareTo(getTotalSoldierCount()) != 0) {
            return new SoldierRatios(getInfantrymanCount().divide(getTotalSoldierCount()).doubleValue(), getRifleCount().divide(getTotalSoldierCount()).doubleValue(),
                    getCannonCount().divide(getTotalSoldierCount()).doubleValue(), getShipCount().divide(getTotalSoldierCount()).doubleValue());
        }
        return new SoldierRatios(0, 0, 0, 0);
    }
}
