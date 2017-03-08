package com.beeva.trustedoverlord.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.beeva.trustedoverlord.model.ProfileSupportCases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public class SupportDynamoDBDao implements SupportDao {
    public boolean saveData(String profileName, ProfileSupportCases profileSupportCases) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(System.getenv("AWS_DYNAMODB_ENDPOINT"),
                        System.getenv("AWS_DYNAMODB_REGION")))
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("overlord");

        String type = "health" + "-" + profileName;


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = now.format(formatter);

        List<Map<String, String>> openSupportCases = Collections.synchronizedList(new ArrayList<>());
        profileSupportCases.getOpenCases().forEach(supportCase -> {
            Map<String, String> mapOpenSupportCases = new HashMap<String, String>();
            mapOpenSupportCases.put("id", supportCase.getId());
            mapOpenSupportCases.put("created", supportCase.getCreated());
            mapOpenSupportCases.put("status", supportCase.getStatus());
            mapOpenSupportCases.put("subject", supportCase.getSubject());
            mapOpenSupportCases.put("submittedby", supportCase.getSubmittedBy());
            openSupportCases.add(mapOpenSupportCases);
                }
        );


        List<Map<String, String>> resolvedSupportCases = Collections.synchronizedList(new ArrayList<>());
        profileSupportCases.getResolvedCases().forEach(supportCase -> {
            Map<String, String> mapResolvedSupportCases = new HashMap<String, String>();
            mapResolvedSupportCases.put("id", supportCase.getId());
            mapResolvedSupportCases.put("created", supportCase.getCreated());
            mapResolvedSupportCases.put("status", supportCase.getStatus());
            mapResolvedSupportCases.put("subject", supportCase.getSubject());
            mapResolvedSupportCases.put("submittedby", supportCase.getSubmittedBy());
            openSupportCases.add(mapResolvedSupportCases);
                }
        );

        final Map<String, Object> infoMap = new HashMap<String, Object>();

        infoMap.put("openCases",  openSupportCases);
        infoMap.put("resolvedCases",  resolvedSupportCases);

        try {
            table.putItem(new Item()
                    .withPrimaryKey("otype", type, "odate", date)
                    .withMap("info", infoMap));
            logger.info("PutItem succeeded: " + type + " " + date);
        } catch (Exception e) {
            logger.info("Unable to add item: " + type + " " + date);
        }

        return true;
    }

    public ProfileSupportCases getData(String profile) {
        return null;
    }

}