package com.islandempires.islandservice.dto.initial;

import com.islandempires.islandservice.enums.IslandBuildingEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InitialBuildingDTO implements Serializable {
    @Id
    protected IslandBuildingEnum islandBuildingEnum;

    private int initialLvl = 0;
}
