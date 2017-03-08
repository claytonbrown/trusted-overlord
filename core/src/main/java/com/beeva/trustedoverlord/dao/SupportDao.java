package com.beeva.trustedoverlord.dao;

import com.beeva.trustedoverlord.model.ProfileSupportCases;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by gonzaloramos on 13/02/17.
 */
public interface SupportDao {
    static Logger logger = LogManager.getLogger(SupportDao.class);

    boolean saveData(String profileName, ProfileSupportCases profileSupportCases) throws Exception;

    ProfileSupportCases getData(String profile) throws Exception;
}
