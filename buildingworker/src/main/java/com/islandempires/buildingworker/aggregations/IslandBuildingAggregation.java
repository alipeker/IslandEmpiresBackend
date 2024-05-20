package com.islandempires.buildingworker.aggregations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Component
public class IslandBuildingAggregation {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Scheduled(fixedRate = 2000)
    public void deduplicateToTargetCollection() {
        AggregationOperation project1 = Aggregation.project()
                .and("_id").as("_id")
                .and("serverId").as("serverId")
                .and("islandId").as("islandId")
                .and("islandBuildingEnum").as("islandBuildingEnum")
                .and("rawMaterialsAndPopulationCost").as("rawMaterialsAndPopulationCost")
                .and("initialLvl").as("initialLvl")
                .and("nextLvl").as("nextLvl")
                .and("constructionDuration").as("constructionDuration")
                .and("remainingTime").as("remainingTime")
                .and("startingDateTimestamp").as("startingDateTimestamp")
                .and("lastCalculatedTimestamp").as("lastCalculatedTimestamp")
                .and("_class").as("_class");


        AggregationOperation project2 = Aggregation.project()
                .and("_id").as("_id")
                .and("serverId").as("serverId")
                .and("islandId").as("islandId")
                .and("islandBuildingEnum").as("islandBuildingEnum")
                .and("rawMaterialsAndPopulationCost").as("rawMaterialsAndPopulationCost")
                .and("initialLvl").as("initialLvl")
                .and("nextLvl").as("nextLvl")
                .and("constructionDuration").as("constructionDuration")
                .and("remainingTime").as("remainingTime")
                .and("startingDateTimestamp").as("startingDateTimestamp")
                .and("_class").as("_class")
                .and(ConditionalOperators.Cond.newBuilder().when(
                                ComparisonOperators.Lte.valueOf(ArithmeticOperators.Subtract.valueOf(new Date().getTime()).subtract("$remainingTime"))
                                        .lessThanEqualTo("$startingDateTimestamp"))
                        .then(0)
                        .otherwise(1)
                ).as("done");

        AggregationOperation project3 = Aggregation
                .addFields()
                .addField("lastCalculatedTimestamp").withValue(new Date().getTime()).build();

        AggregationOperation matchStage = Aggregation.match(
                Criteria.where("done").is(1)
        );

        AggregationOperation project4 = Aggregation.merge().into(MergeOperation.MergeOperationTarget.collection("BuildingScheduledTaskDone")).build();

        List<AggregationOperation> pipeline = new ArrayList<>();
        pipeline.add(project1);
        pipeline.add(project2);
        pipeline.add(project3);
        pipeline.add(matchStage);
        pipeline.add(project4);

        List<Object> buildingScheduledList = mongoTemplate.aggregate(newAggregation(pipeline), "BuildingScheduledTask", Object.class).getMappedResults();

        buildingScheduledList.forEach(buildingScheduled -> {
            mongoTemplate.remove(buildingScheduled, "BuildingScheduledTask");
        });

    }



}
