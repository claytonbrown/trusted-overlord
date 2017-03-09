package com.beeva.trustedoverlord.dao.tests;

import com.amazonaws.AmazonClientException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.handlers.AsyncHandler;
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
@RunWith(MockitoJUnitRunner.class)
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
        errors.add("ERROR: 1");
        errors.add("ERROR: 2");
        warnings.add("WARNING: 1");
        exceptions.add(new Exception("EXCEPTION: 1"));


        when(mockProfile.getErrors()).thenReturn(errors);
        when(mockProfile.getWarnings()).thenReturn(warnings);
        when(mockProfile.getExceptions()).thenReturn(exceptions);

        // Calls the method
        TrustedAdvisorDynamoDBDao trustedDAO = new TrustedAdvisorDynamoDBDao();
        boolean result = trustedDAO.saveData("test-profile", mockProfile);

        Assert.assertThat(result, is(true));


        /*
        // Capture the parameters when invoking the method describeTrustedAdvisorChecksAsync (first callback)
        doAnswer(invocation -> {
            DescribeTrustedAdvisorChecksRequest request = invocation.getArgument(0);
            AsyncHandler<DescribeTrustedAdvisorChecksRequest, DescribeTrustedAdvisorChecksResult> handler =
                    invocation.getArgument(1);

            DescribeTrustedAdvisorChecksResult result = createChecks();
            handler.onSuccess(request, result);

            Assert.assertThat(request.getLanguage(), is(Locale.ENGLISH.getLanguage()));

            return CompletableFuture.completedFuture(result);
        }).when(mockClient).describeTrustedAdvisorChecksAsync(any(DescribeTrustedAdvisorChecksRequest.class), any());

        // Capture the parameters when invoking the method describeTrustedAdvisorCheckResultAsync (second callback)
        DescribeTrustedAdvisorCheckResultResult describeTrustedAdvisorCheckResultResult = createChecksResult();
        doAnswer(invocation -> {
            DescribeTrustedAdvisorCheckResultRequest request = invocation.getArgument(0);
            AsyncHandler<DescribeTrustedAdvisorCheckResultRequest, DescribeTrustedAdvisorCheckResultResult> handler =
                    invocation.getArgument(1);

            handler.onSuccess(request, describeTrustedAdvisorCheckResultResult);

            Assert.assertThat(request.getLanguage(), is(Locale.ENGLISH.getLanguage()));

            return CompletableFuture.completedFuture(describeTrustedAdvisorCheckResultResult);
        }).when(mockClient).describeTrustedAdvisorCheckResultAsync(any(DescribeTrustedAdvisorCheckResultRequest.class), any());

        // Calls the method
        TrustedAdvisorClient trustedClient = TrustedAdvisorClient.withClient(mockClient);
        CompletableFuture<ProfileChecks> future = (CompletableFuture<ProfileChecks>) trustedClient.getProfileChecks();

        Assert.assertThat(trustedClient.isAutoshutdown(), is(false));

        // Waits until the future is complete
        Awaitility.await().until(future::isDone);

        Assert.assertThat(future.get().getErrors().isEmpty(), is(false));
        Assert.assertThat(future.get().getErrors().size(), is(2));
        Assert.assertThat(future.get().getErrors().get(0), is("Name 1"));
        Assert.assertThat(future.get().getExceptions().isEmpty(), is(true));
        Assert.assertThat(future.get().getWarnings().isEmpty(), is(false));
        Assert.assertThat(future.get().getWarnings().size(), is(2));
        Assert.assertThat(future.get().getWarnings().get(1), is("Name 4"));

        verify(mockClient).describeTrustedAdvisorChecksAsync(any(DescribeTrustedAdvisorChecksRequest.class), any());
        verify(mockClient, times(4)).describeTrustedAdvisorCheckResultAsync(any(DescribeTrustedAdvisorCheckResultRequest.class), any());
        verify(mockClient, never()).shutdown();*/

    }
}
