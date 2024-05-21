package com.islandempires.resourceworker.aggregations;

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
public class IslandResourceAggregation {


    @Autowired
    private MongoTemplate mongoTemplate;


    @Scheduled(fixedRate = 5000)
    public void deduplicateToTargetCollection() {
        AggregationOperation project1 = Aggregation.project()
                .and("_id").as("_id")
                .and("userId").as("userId")
                .and("serverId").as("serverId")
                .and("wood").as("wood")
                .and("woodHourlyProduction").as("woodHourlyProduction")
                .and("iron").as("iron")
                .and("ironHourlyProduction").as("ironHourlyProduction")
                .and("ironHourlyProductionMultiply").as("ironHourlyProductionMultiply")
                .and("clay").as("clay")
                .and("clayHourlyProduction").as("clayHourlyProduction")
                .and("clayHourlyProductionMultiply").as("clayHourlyProductionMultiply")
                .and("rawMaterialStorageSize").as("rawMaterialStorageSize")
                .and("meatFoodCoefficient").as("meatFoodCoefficient")
                .and("meatHourlyProduction").as("meatHourlyProduction")
                .and("fishFoodCoefficient").as("fishFoodCoefficient")
                .and("fishHourlyProduction").as("fishHourlyProduction")
                .and("wheatFoodCoefficient").as("wheatFoodCoefficient")
                .and("wheatHourlyProduction").as("wheatHourlyProduction")
                .and("population").as("population")
                .and("temporaryPopulation").as("temporaryPopulation")
                .and("populationLimit").as("populationLimit")
                .and("happinessScore").as("happinessScore")
                .and("additionalHappinessScore").as("additionalHappinessScore")
                .and("happinessScoreMinimumValue").as("happinessScoreMinimumValue")
                .and("happinessScoreMaximumValue").as("happinessScoreMaximumValue")
                .and("createdDate").as("createdDate")
                .and("_class").as("_class")
                .and(ArithmeticOperators.Subtract.valueOf(new Date().getTime()).subtract("lastCalculatedTimestamp")).as("elapsedTime");

        AggregationOperation project2 = Aggregation.project()
                .and("_id").as("_id")
                .and("userId").as("userId")
                .and("serverId").as("serverId")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("wood").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("woodHourlyProduction").divideBy(60))))).greaterThan("rawMaterialStorageSize"))
                        .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("wood").add(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("woodHourlyProduction").divideBy(60)))))).as("wood")
                .and("woodHourlyProduction").as("woodHourlyProduction")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(
                                ArithmeticOperators.Add.valueOf("iron").add(ArithmeticOperators.Multiply.valueOf(
                                        ArithmeticOperators.Add.valueOf(ArithmeticOperators.valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("ironHourlyProduction").divideBy(60)))).multiplyBy(
                                        ArithmeticOperators.Add.valueOf(1).add(ArithmeticOperators.Divide.valueOf("ironHourlyProductionMultiply").divideBy(100))
                                ))
                        ).greaterThan("rawMaterialStorageSize"))
                        .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("iron").add(ArithmeticOperators.Multiply.valueOf(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("ironHourlyProduction").divideBy(60)))).multiplyBy(
                                ArithmeticOperators.Add.valueOf(1).add(ArithmeticOperators.Divide.valueOf("ironHourlyProductionMultiply").divideBy(100))
                        )))).as("iron")
                .and("ironHourlyProduction").as("ironHourlyProduction")
                .and("ironHourlyProductionMultiply").as("ironHourlyProductionMultiply")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gt.valueOf(ArithmeticOperators.Add.valueOf("clay").add(ArithmeticOperators.Multiply.valueOf(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("clayHourlyProduction").divideBy(60)))).multiplyBy(
                                ArithmeticOperators.Add.valueOf(1).add(ArithmeticOperators.Divide.valueOf("clayHourlyProductionMultiply").divideBy(100))
                        ))).greaterThan("rawMaterialStorageSize"))
                        .then("$rawMaterialStorageSize").otherwise(ArithmeticOperators.Add.valueOf("clay").add(ArithmeticOperators.Multiply.valueOf(ArithmeticOperators.Add.valueOf(ArithmeticOperators
                                .valueOf(ArithmeticOperators.Divide.valueOf("elapsedTime").divideBy(60000))
                                .multiplyBy(ArithmeticOperators.Divide.valueOf("clayHourlyProduction").divideBy(60)))).multiplyBy(
                                ArithmeticOperators.Add.valueOf(1).add(ArithmeticOperators.Divide.valueOf("clayHourlyProductionMultiply").divideBy(100))
                        )))).as("clay")
                .and("clayHourlyProduction").as("clayHourlyProduction")
                .and("clayHourlyProductionMultiply").as("clayHourlyProductionMultiply")
                .and("rawMaterialStorageSize").as("rawMaterialStorageSize")
                .and("meatFoodCoefficient").as("meatFoodCoefficient")
                .and("meatHourlyProduction").as("meatHourlyProduction")
                .and("fishFoodCoefficient").as("fishFoodCoefficient")
                .and("fishHourlyProduction").as("fishHourlyProduction")
                .and("wheatFoodCoefficient").as("wheatFoodCoefficient")
                .and("wheatHourlyProduction").as("wheatHourlyProduction")
                .and("population").as("population")
                .and("temporaryPopulation").as("temporaryPopulation")
                .and(ArithmeticOperators.Add.valueOf(ArithmeticOperators.Multiply.valueOf("meatFoodCoefficient").multiplyBy("meatHourlyProduction"))
                        .add(ArithmeticOperators.Multiply.valueOf("fishFoodCoefficient").multiplyBy("fishHourlyProduction"))
                        .add(ArithmeticOperators.Multiply.valueOf("wheatFoodCoefficient").multiplyBy("wheatHourlyProduction"))).as("populationLimit")
                .and(ConditionalOperators.Cond.newBuilder()
                        .when(ComparisonOperators.Gte.valueOf(ArithmeticOperators.Add.valueOf("populationLimit"))
                                .greaterThanEqualTo(ArithmeticOperators.Add.valueOf("population").add("temporaryPopulation")))
                        .then(ArithmeticOperators.Add.valueOf("additionalHappinessScore").add(1))
                        .otherwise(ArithmeticOperators.Add.valueOf("additionalHappinessScore").add(ArithmeticOperators.Divide.valueOf("populationLimit")
                                        .divideBy(ArithmeticOperators.Add.valueOf("population").add("temporaryPopulation"))
                        ))).as("happinessScore")
                .and("additionalHappinessScore").as("additionalHappinessScore")
                .and("_class").as("_class");

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
