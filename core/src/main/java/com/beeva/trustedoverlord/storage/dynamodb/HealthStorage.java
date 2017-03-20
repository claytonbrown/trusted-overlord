package com.beeva.trustedoverlord.storage.dynamodb;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileHealth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class HealthStorage implements DynamoDBStorage<ProfileHealth> {
    private static Logger logger = LogManager.getLogger(HealthStorage.class);

    private AmazonDynamoDBAsync dynamoClient;

    public HealthStorage() {
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

    public void save(String profileName, ProfileHealth profileHealth, final CompletableFuture<PutItemResult> future) {
        String type = "health" + "-" + profileName;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<>();
        item.put("otype", new AttributeValue(type));
        item.put("odate", new AttributeValue(date));
        item.put("openIssues",  new AttributeValue().withSS(profileHealth.getOpenIssues()));
        item.put("scheduledChanges",  new AttributeValue().withSS(profileHealth.getScheduledChanges()));
        item.put("otherNotifications",  new AttributeValue().withSS(profileHealth.getOtherNotifications()));

        this.dynamoClient.putItemAsync(
            new PutItemRequest()
                    .withTableName("overlord")
                    .withItem(item),
            new AsyncHandler<PutItemRequest, PutItemResult>() {
                @Override
                public void onError(Exception exception) {
                    logger.error("Unable to store item: {} {}", type, date);
                    future.completeExceptionally(exception);
                }

                @Override
                public void onSuccess(PutItemRequest request, PutItemResult putItemRes) {
                    logger.debug("PutItem succeeded: {} {}", type, date);
                    future.complete(putItemRes);
                }
            }
        );
    }
}