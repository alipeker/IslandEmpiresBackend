package com.islandempires.islandservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateOwnerDTO {
    private String islandID;
    private Long userId;
}
