package com.beeva.trustedoverlord.dao;

import com.beeva.trustedoverlord.model.ProfileChecks;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public class TrustedAdvisorDynamoDBDao implements TrustedAdvisorDao {
    public boolean saveData(ProfileChecks profileChecks) {
        return true;
    }

    public ProfileChecks getData(String profile) {
        return null;
    }

}
