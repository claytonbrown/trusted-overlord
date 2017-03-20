package com.beeva.trustedoverlord.storage.dynamodb;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileChecks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;


public class TrustedAdvisorStorage implements DynamoDBStorage<ProfileChecks> {
    private static Logger logger = LogManager.getLogger(TrustedAdvisorStorage.class);
    private AmazonDynamoDBAsync dynamoClient;

    public TrustedAdvisorStorage() {
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

    public void save(String profileName, ProfileChecks profileChecks, final CompletableFuture<PutItemResult> future) {
        String type = "trustedadvisor" + "-" + profileName;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        final Map<String, AttributeValue> item = new HashMap<>();
        item.put("otype", new AttributeValue(type));
        item.put("odate", new AttributeValue(date));
        item.put("errors", new AttributeValue().withSS(profileChecks.getErrors()));
        item.put("warnings", new AttributeValue().withSS(profileChecks.getWarnings()));


        List<String> exceptions = Collections.synchronizedList(new ArrayList<>());
        profileChecks.getExceptions().forEach(ex -> exceptions.add(ex.getMessage()));
        item.put("exceptions",  new AttributeValue().withSS(exceptions));

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