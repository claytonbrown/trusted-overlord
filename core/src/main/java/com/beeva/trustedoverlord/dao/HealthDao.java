package com.beeva.trustedoverlord.dao;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileHealth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;


public interface HealthDao {
    static Logger logger = LogManager.getLogger(HealthDao.class);

    public void saveDataAsync(String profileName, ProfileHealth profileHealth, final CompletableFuture<PutItemResult> future) throws Exception;
}
