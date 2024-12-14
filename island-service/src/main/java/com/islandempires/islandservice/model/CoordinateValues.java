package com.islandempires.islandservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateValues {
    private int t;
    private int lastActiveX;
    private int lastActiveY;
    private int lastX;
    private int lastY;
    private int c;
}

