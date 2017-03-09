package com.beeva.trustedoverlord.dao;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
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
import java.util.concurrent.CompletableFuture;


public class TrustedAdvisorDynamoDBDao implements TrustedAdvisorDao {
    private AmazonDynamoDBAsync dynamoClient;

    public TrustedAdvisorDynamoDBDao() {
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

    public void saveDataAsync(String profileName, ProfileChecks profileChecks, final CompletableFuture<PutItemResult> future) {
        String type = "trustedadvisor" + "-" + profileName;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("otype", new AttributeValue(type));
        item.put("odate", new AttributeValue(date));
        item.put("errors", new AttributeValue().withSS(profileChecks.getErrors()));
        item.put("warnings", new AttributeValue().withSS(profileChecks.getWarnings()));


        List<String> exceptions = Collections.synchronizedList(new ArrayList<>());
        profileChecks.getExceptions().forEach(ex -> {
            exceptions.add(ex.getMessage());
        });
        item.put("exceptions",  new AttributeValue().withSS(exceptions));

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