package com.beeva.trustedoverlord.dao;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileHealth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


public class HealthDynamoDBDao implements HealthDao {
    private AmazonDynamoDBAsync dynamoClient;

    public HealthDynamoDBDao() {
        this.dynamoClient = AmazonDynamoDBAsyncClientBuilder
                .standard()
                .withCredentials(new ProfileCredentialsProvider("default"))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                System.getenv("AWS_DYNAMODB_ENDPOINT"),
                                System.getenv("AWS_DYNAMODB_REGION")
                        )
                )
                .build();
    }

    public void saveDataAsync(String profileName, ProfileHealth profileHealth, final CompletableFuture<PutItemResult> future) {
        String type = "health" + "-" + profileName;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("otype", new AttributeValue(type));
        item.put("odate", new AttributeValue(date));
        item.put("openIssues",  new AttributeValue().withSS(profileHealth.getOpenIssues()));
        item.put("scheduledChanges",  new AttributeValue().withSS(profileHealth.getScheduledChanges()));
        item.put("otherNotifications",  new AttributeValue().withSS(profileHealth.getOtherNotifications()));

        Future<PutItemResult> putItemResult = this.dynamoClient.putItemAsync(
                new PutItemRequest()
                        .withTableName("overlord")
                        .withItem(item),
                new AsyncHandler<PutItemRequest, PutItemResult>() {
                    @Override
                    public void onError(Exception exception) {
                        logger.error("Unable to add item: " + type + " " + date);
                        future.completeExceptionally(exception);
                    }

                    @Override
                    public void onSuccess(PutItemRequest request, PutItemResult putItemRes) {
                        logger.info("PutItem succeeded: " + type + " " + date);
                        future.complete(putItemRes);
                    }
                }
        );
    }
}