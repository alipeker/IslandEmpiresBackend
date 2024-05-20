package com.islandempires.buildingservice.util;

import com.islandempires.buildingservice.enums.IslandBuildingEnum;
import com.islandempires.buildingservice.model.building.AllBuildings;
import com.islandempires.buildingservice.model.building.Building;
import com.islandempires.buildingservice.shared.building.AllBuildingsServerProperties;
import com.islandempires.buildingservice.shared.buildingtype.BaseStructures;

import java.lang.reflect.Method;

public class FindInListWithField {
    public static Building findBuilding(AllBuildings allBuildings, IslandBuildingEnum islandBuildingEnum) {
        Method[] methods = AllBuildings.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    Building building = (Building) method.invoke(allBuildings);
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

    public static BaseStructures findBuildingProperties(AllBuildingsServerProperties allBuildings, IslandBuildingEnum islandBuildingEnum) {
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
