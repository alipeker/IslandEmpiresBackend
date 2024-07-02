package com.islandempires.militaryservice.dto.request;

import com.islandempires.militaryservice.enums.SoldierSubTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSoldierRequest {

    private int soldierCount;

    private SoldierSubTypeEnum soldierSubType;

}
