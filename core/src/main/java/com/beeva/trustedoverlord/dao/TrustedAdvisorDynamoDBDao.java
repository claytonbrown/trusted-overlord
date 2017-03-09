package com.beeva.trustedoverlord.dao;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsAsyncClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileChecks;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public class TrustedAdvisorDynamoDBDao implements TrustedAdvisorDao {
    private AmazonDynamoDBAsync dynamoClient;

    public TrustedAdvisorDynamoDBDao() {
        this.dynamoClient = AmazonDynamoDBAsyncClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider("default"))
                .withRegion(Regions.EU_WEST_1)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                System.getenv("AWS_DYNAMODB_ENDPOINT"),
                                System.getenv("AWS_DYNAMODB_REGION")
                        )
                )
                .build();
    }

    public boolean saveData(String profileName, ProfileChecks profileChecks) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("otype", new AttributeValue("trustedadvisor" + "-" + profileName));
        item.put("odate", new AttributeValue(date));
        item.put("errors", new AttributeValue().withSS(profileChecks.getErrors()));
        item.put("warnings", new AttributeValue().withSS(profileChecks.getWarnings()));

        Future<PutItemResult> putItemRes = this.dynamoClient.putItemAsync(new PutItemRequest()
                .withTableName("overlord")
                .withItem(item)
        );

        /*try {
            table.putItem(new Item()
                    .withPrimaryKey("otype", type, "odate", date)
                    .withMap("info", infoMap));
            logger.info("PutItem succeeded: " + type + " " + date);
        } catch (Exception e) {
            logger.error("Unable to add item: " + type + " " + date);
        }*/

        return true;
    }

    public ProfileChecks getData(String profile) {
        return null;
    }

}