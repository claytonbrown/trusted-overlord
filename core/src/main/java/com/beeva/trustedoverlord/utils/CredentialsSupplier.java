package com.beeva.trustedoverlord.utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;

/**
 * Created by BEEVA
 */
public class CredentialsSupplier {

    public static AWSCredentialsProvider getAWSCredentialsProvider(String profile) {

        BasicProfile basicProfile = new ProfilesConfigFile().getAllBasicProfiles().get(profile);
        if(basicProfile == null) {
            throw new RuntimeException("No AWS profile named '" + profile + "'");
        }

        if (basicProfile.isRoleBasedProfile()) {
            return new STSAssumeRoleSessionCredentialsProvider.Builder(basicProfile.getRoleArn(), profile)
                    .withStsClient(AWSSecurityTokenServiceClientBuilder.defaultClient()).build();
        } else {
            return new ProfileCredentialsProvider(profile);
        }
    }

}
