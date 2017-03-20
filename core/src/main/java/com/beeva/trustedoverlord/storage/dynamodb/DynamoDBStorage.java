package com.beeva.trustedoverlord.storage.dynamodb;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.storage.Storage;

/**
 * Created by BEEVA
 */
public interface DynamoDBStorage<I> extends Storage<I, PutItemResult> {
}
