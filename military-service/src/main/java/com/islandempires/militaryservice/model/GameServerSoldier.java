package com.islandempires.militaryservice.model;

import com.islandempires.militaryservice.model.soldier.SoldierBaseInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameServerSoldier {

    @Id
    private String id;

    public GameServerSoldier(String id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "gameServerSoldier", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SoldierBaseInfo> soldierBaseInfoList;

    @Override
    public String toString() {
        return "GameServerSoldier{" +
                "id='" + id + '\'' +
                '}';
    }
}
