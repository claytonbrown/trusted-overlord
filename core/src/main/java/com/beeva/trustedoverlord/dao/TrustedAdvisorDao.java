package com.beeva.trustedoverlord.dao;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileChecks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public interface TrustedAdvisorDao {
    static Logger logger = LogManager.getLogger(TrustedAdvisorDao.class);

    public void saveDataAsync(String profileName, ProfileChecks profileChecks,
                               final CompletableFuture<PutItemResult>future) throws Exception;
}
