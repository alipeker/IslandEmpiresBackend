package com.islandempires.islandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateIslandDTO {
    private String islandID;
    private String name;
}
