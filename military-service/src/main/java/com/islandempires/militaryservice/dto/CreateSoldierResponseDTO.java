package com.islandempires.militaryservice.dto;

import com.islandempires.militaryservice.model.IslandMilitary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSoldierResponseDTO implements Serializable {
    private IslandResourceDTO islandResourceDTO;

    private IslandMilitary islandMilitary;
}
