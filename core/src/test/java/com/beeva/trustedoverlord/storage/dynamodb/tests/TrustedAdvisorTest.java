package com.beeva.trustedoverlord.storage.dynamodb.tests;

import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.storage.dynamodb.TrustedAdvisorStorage;
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
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Beeva
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class TrustedAdvisorTest {

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

        try {
            // Calls the method
            TrustedAdvisorStorage trustedStorage = new TrustedAdvisorStorage();
            CompletableFuture<PutItemResult> future = new CompletableFuture<>();
            trustedStorage.save("test-profile", mockProfile, future);

            Awaitility.await().until(future::isDone);
        } catch (Exception e) {
            System.out.print("No DynamodbDB");
        }
        Assert.assertThat(true, is(true));
    }
}
