package com.beeva.trustedoverlord.dao;

import com.beeva.trustedoverlord.clients.SupportClient;
import com.beeva.trustedoverlord.model.ProfileChecks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public interface TrustedAdvisorDao {
    static Logger logger = LogManager.getLogger(SupportClient.class);

    boolean saveData(String profileName, ProfileChecks profileChecks) throws Exception;

    ProfileChecks getData(String profile) throws Exception;
}
