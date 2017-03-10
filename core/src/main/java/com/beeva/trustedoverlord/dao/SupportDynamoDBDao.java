package com.beeva.trustedoverlord.dao;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileSupportCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


public class SupportDynamoDBDao implements SupportDao {
    private AmazonDynamoDBAsync dynamoClient;

    public SupportDynamoDBDao() {
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


    public void saveDataAsync(String profileName, ProfileSupportCases profileSupportCases,
                              final CompletableFuture<PutItemResult> future) {
        String type = "support" + "-" + profileName;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("otype", new AttributeValue(type));
        item.put("odate", new AttributeValue(date));

        List<AttributeValue> openSupportCases = Collections.synchronizedList(new ArrayList<>());
        profileSupportCases.getOpenCases().forEach(supportCase -> {
                    Map<String, AttributeValue> mapOpenSupportCase = new HashMap<String, AttributeValue>();
                    mapOpenSupportCase.put("id", new AttributeValue(supportCase.getId()));
                    mapOpenSupportCase.put("created", new AttributeValue(supportCase.getCreated()));
                    mapOpenSupportCase.put("status", new AttributeValue(supportCase.getStatus()));
                    mapOpenSupportCase.put("subject", new AttributeValue(supportCase.getSubject()));
                    mapOpenSupportCase.put("submittedby", new AttributeValue(supportCase.getSubmittedBy()));

                    openSupportCases.add(new AttributeValue().withM(mapOpenSupportCase));
                }
        );
        item.put("openCases", new AttributeValue().withL(openSupportCases));

        List<AttributeValue> resolvedSupportCases = Collections.synchronizedList(new ArrayList<>());
        profileSupportCases.getResolvedCases().forEach(supportCase -> {
                    Map<String, AttributeValue> mapResolvedSupportCase = new HashMap<String, AttributeValue>();
                    mapResolvedSupportCase.put("id", new AttributeValue(supportCase.getId()));
                    mapResolvedSupportCase.put("created", new AttributeValue(supportCase.getCreated()));
                    mapResolvedSupportCase.put("status", new AttributeValue(supportCase.getStatus()));
                    mapResolvedSupportCase.put("subject", new AttributeValue(supportCase.getSubject()));
                    mapResolvedSupportCase.put("submittedby", new AttributeValue(supportCase.getSubmittedBy()));

                    resolvedSupportCases.add(new AttributeValue().withM(mapResolvedSupportCase));
                }
        );
        item.put("resolvedCases", new AttributeValue().withL(openSupportCases));

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