package com.beeva.trustedoverlord.dao;

import com.beeva.trustedoverlord.model.ProfileHealth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public interface HealthDao {
    static Logger logger = LogManager.getLogger(HealthDao.class);

    boolean saveData(String profileName, ProfileHealth profileHealth) throws Exception;

    ProfileHealth getData(String profile) throws Exception;
}
