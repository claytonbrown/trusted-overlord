package com.beeva.trustedoverlord.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.beeva.trustedoverlord.model.ProfileHealth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class HealthDynamoDBDao implements HealthDao {
    public boolean saveData(String profileName, ProfileHealth profileHealth) {
        AmazonDynamoDBAsync client = AmazonDynamoDBAsyncClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(System.getenv("AWS_DYNAMODB_ENDPOINT"),
                        System.getenv("AWS_DYNAMODB_REGION")))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("overlord");

        String type = "health" + "-" + profileName;


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("openIssues",  profileHealth.getOpenIssues());
        infoMap.put("scheduledChanges",  profileHealth.getScheduledChanges());
        infoMap.put("otherNotifications",  profileHealth.getOtherNotifications());

        try {
            table.putItem(new Item()
                    .withPrimaryKey("otype", type, "odate", date)
                    .withMap("info", infoMap));
            logger.info("PutItem succeeded: " + type + " " + date);
        } catch (Exception e) {
            logger.error("Unable to add item: " + type + " " + date);
        }

        return true;
    }

    public ProfileHealth getData(String profile) {
        return null;
    }

}