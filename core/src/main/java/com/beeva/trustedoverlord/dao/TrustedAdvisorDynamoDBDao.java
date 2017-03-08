package com.beeva.trustedoverlord.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.beeva.trustedoverlord.model.ProfileChecks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public class TrustedAdvisorDynamoDBDao implements TrustedAdvisorDao {
    public boolean saveData(String profileName, ProfileChecks profileChecks) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(System.getenv("AWS_DYNAMODB_ENDPOINT"),
                        System.getenv("AWS_DYNAMODB_REGION")))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("overlord");

        String type = "trustedadvisor" + "-" + profileName;


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("errors",  profileChecks.getErrors());
        infoMap.put("warnings",  profileChecks.getWarnings());

        List<String> exceptions = Collections.synchronizedList(new ArrayList<>());
        profileChecks.getExceptions().forEach(ex -> {
            exceptions.add(ex.getMessage());
                });
        infoMap.put("exceptions",  exceptions);

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

    public ProfileChecks getData(String profile) {
        return null;
    }

}