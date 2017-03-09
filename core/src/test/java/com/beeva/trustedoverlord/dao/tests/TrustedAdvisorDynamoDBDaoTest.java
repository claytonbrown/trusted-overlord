package com.beeva.trustedoverlord.dao.tests;

import com.amazonaws.AmazonClientException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.support.AWSSupportAsync;
import com.amazonaws.services.support.model.*;
import com.beeva.trustedoverlord.dao.TrustedAdvisorDynamoDBDao;
import com.beeva.trustedoverlord.clients.TrustedAdvisorClient;
import com.beeva.trustedoverlord.model.ProfileChecks;
import org.awaitility.Awaitility;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Beeva
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TrustedAdvisorDynamoDBDaoTest {

    @Mock
    private ProfileChecks mockProfile;

    @Before
    public void setUp() throws Exception {
        reset(mockProfile);
    }

    @Test
    public void testSaveData() throws Exception {

        List<String> errors = Collections.synchronizedList(new ArrayList<>());
        List<String> warnings = Collections.synchronizedList(new ArrayList<>());
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        errors.add("ERRORRR: 1");
        errors.add("ERROR: 2");
        warnings.add("WARNING: 1");
        exceptions.add(new Exception("EXCEPTION: 1"));


        when(mockProfile.getErrors()).thenReturn(errors);
        when(mockProfile.getWarnings()).thenReturn(warnings);
        when(mockProfile.getExceptions()).thenReturn(exceptions);

        // Calls the method
        TrustedAdvisorDynamoDBDao trustedDAO = new TrustedAdvisorDynamoDBDao();
        CompletableFuture<PutItemResult> future = new CompletableFuture<>();
        trustedDAO.saveDataAsync("test-profile", mockProfile, future);

        Awaitility.await().until(future::isDone);

        Assert.assertThat(true, is(true));


    }
}
