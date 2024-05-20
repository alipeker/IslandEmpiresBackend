package com.islandempires.buildingworker.util;

import com.islandempires.buildingworker.enums.IslandBuildingEnum;
import com.islandempires.buildingworker.shared.building.AllBuildingsServerProperties;
import com.islandempires.buildingworker.shared.buildingtype.BaseStructures;

import java.lang.reflect.Method;

public class FindClassField {

    public static BaseStructures findBuildingProperty(AllBuildingsServerProperties allBuildings, IslandBuildingEnum islandBuildingEnum) {
        Method[] methods = AllBuildingsServerProperties.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    BaseStructures building = (BaseStructures) method.invoke(allBuildings);
                    if(building != null && building.getIslandBuildingEnum().equals(islandBuildingEnum)) {
                        return building;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
