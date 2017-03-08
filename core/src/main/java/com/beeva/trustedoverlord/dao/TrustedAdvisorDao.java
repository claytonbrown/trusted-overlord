package com.beeva.trustedoverlord.dao;

import com.beeva.trustedoverlord.model.ProfileChecks;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public interface TrustedAdvisorDao {

    boolean saveData(ProfileChecks profileChecks) throws Exception;

    ProfileChecks getData(String profile) throws Exception;
}
