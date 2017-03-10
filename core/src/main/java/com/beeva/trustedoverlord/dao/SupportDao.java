package com.beeva.trustedoverlord.dao;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.model.ProfileSupportCases;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;


public interface SupportDao {
    static Logger logger = LogManager.getLogger(SupportDao.class);

    public void saveDataAsync(String profileName, ProfileSupportCases profileSupportCases,
                              final CompletableFuture<PutItemResult> future) throws Exception;
}
