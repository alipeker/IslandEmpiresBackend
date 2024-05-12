package com.islandempires.buildingworker.aggregations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
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


    @Scheduled(fixedRate = 5000)
    public void deduplicateToTargetCollection() {
        AggregationOperation project1 = Aggregation.project()
                .and("_id").as("_id")
                .and("_class").as("_class")
                .and("islandId").as("islandId")
                .and("islandBuildingEnum").as("islandBuildingEnum")
                .and("initialLvl").as("initialLvl")
                .and("nextLvl").as("nextLvl")
                .and("constructionDuration").as("constructionDuration")
                .and("remainingTime").as("remainingTime")
                .and("startingDate").as("startingDate")
                .and("0").as("test")
                .and(ArithmeticOperators.Subtract.valueOf(new Date().getTime()).subtract("lastCalculatedTimestamp")).as("elapsedTime");

        AggregationOperation project2 = Aggregation.project()
                .and("_id").as("_id")
                .and("_class").as("_class")
                .and("islandId").as("islandId")
                .and("islandBuildingEnum").as("islandBuildingEnum")
                .and("initialLvl").as("initialLvl")
                .and("nextLvl").as("nextLvl")
                .and("constructionDuration").as("constructionDuration")
                .and("elapsedTime").as("elapsedTime")
                .and("remainingTime").as("remainingTime")
                .and(ConditionalOperators.Cond.newBuilder().when(
                                ComparisonOperators.Lte.valueOf(ArithmeticOperators.Subtract.valueOf("$remainingTime").subtract("$elapsedTime"))
                                        .lessThanEqualToValue("0"))
                                .then(ArithmeticOperators.Subtract.valueOf("$remainingTime").subtract("$elapsedTime"))
                                .otherwise(0)
                ).as("remainingTime");

        AggregationOperation project3 = Aggregation
                .addFields()
                .addField("lastCalculatedTimestamp").withValue(new Date().getTime()).build();

        AggregationOperation project4 = Aggregation.out("BuildingScheduledTask");

        List<AggregationOperation> pipeline = new ArrayList<>();
        pipeline.add(project1);
        pipeline.add(project2);
        pipeline.add(project3);
        pipeline.add(project4);

        // Run the aggregation pipeline
        mongoTemplate.aggregate(newAggregation(pipeline), "BuildingScheduledTask", Object.class);
    }


}
