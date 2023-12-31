package com.islandempires.resourceworker.aggregations;

import com.islandempires.resourceworker.mongodb.IslandResource;
import com.islandempires.resourceworker.repository.IslandResourceRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClients;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Component
public class IslandResourceAggregation {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IslandResourceRepository islandResourceRepository;

    @Scheduled(fixedRate = 5000)
    public void deduplicateToTargetCollection() {
        AggregationOperation project1 = Aggregation.project()
                .and("_id").as("_id")
                .and("wood").as("wood")
                .and("hourlyWoodProduction").as("hourlyWoodProduction")
                .and("iron").as("iron")
                .and("hourlyIronProduction").as("hourlyIronProduction")
                .and("clay").as("clay")
                .and("hourlyClayProduction").as("hourlyClayProduction")
                .and("gold").as("gold")
                .and("hourlyGoldProduction").as("hourlyGoldProduction")
                .and("rawMaterialStorageSize").as("rawMaterialStorageSize")
                .and("meat").as("meat")
                .and("fish").as("fish")
                .and("wheat").as("wheat")
                .and("foodStorageSize").as("foodStorageSize")
                .and("population").as("population")
                .and("hourlyPopulationGrowth").as("hourlyPopulationGrowth")
                .and("populationLimit").as("populationLimit")
                .and(ArithmeticOperators.Subtract.valueOf(new Date().getTime()).subtract("lastCalculatedTimestamp")).as("elapsedTime");

        AggregationOperation project2 = Aggregation.project()
                .and("_id").as("_id")
                .and("_class").as("_class")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("wood").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                        .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                        .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyWoodProduction").divideBy(60))))).greaterThan("rawMaterialStorageSize"))
                                .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("wood").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                        .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                        .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyWoodProduction").divideBy(60)))))).as("wood")
                .and("hourlyWoodProduction").as("hourlyWoodProduction")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("iron").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyIronProduction").divideBy(60))))).greaterThan("rawMaterialStorageSize"))
                        .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("iron").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyIronProduction").divideBy(60)))))).as("iron")
                .and("hourlyIronProduction").as("hourlyIronProduction")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("clay").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyClayProduction").divideBy(60))))).greaterThan("rawMaterialStorageSize"))
                        .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("clay").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyClayProduction").divideBy(60)))))).as("clay")
                .and("hourlyClayProduction").as("hourlyClayProduction")
                .and(ArithmeticOperators.Add.valueOf("gold").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                        .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                        .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyGoldProduction").divideBy(60))))).as("gold")
                .and("hourlyGoldProduction").as("hourlyGoldProduction")
                .and("rawMaterialStorageSize").as("rawMaterialStorageSize")
                .and("meat").as("meat")
                .and("fish").as("fish")
                .and("wheat").as("wheat")
                .and("foodStorageSize").as("foodStorageSize")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("population").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyPopulationGrowth").divideBy(60))))).greaterThan("populationLimit"))
                        .then("$populationLimit").otherwise(ArithmeticOperators.Add.valueOf("population").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("hourlyPopulationGrowth").divideBy(60)))))).as("population")
                .and("hourlyPopulationGrowth").as("hourlyPopulationGrowth")
                .and("populationLimit").as("populationLimit");

        AggregationOperation project3 = Aggregation
                .addFields()
                .addField("lastCalculatedTimestamp").withValue(new Date().getTime()).build();

        AggregationOperation project4 = Aggregation.out("IslandResource");

        List<AggregationOperation> pipeline = new ArrayList<>();
        pipeline.add(project1);
        pipeline.add(project2);
        pipeline.add(project3);
        pipeline.add(project4);

        // Run the aggregation pipeline
        mongoTemplate.aggregate(newAggregation(pipeline), "IslandResource", Object.class);
    }


}
