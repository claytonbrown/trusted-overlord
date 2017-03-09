package com.beeva.trustedoverlord.dao.tests;

import com.beeva.trustedoverlord.dao.SupportDynamoDBDao;
import com.beeva.trustedoverlord.model.ProfileSupportCases;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by Beeva
 */
@RunWith(MockitoJUnitRunner.class)
public class SupportDynamoDBDaoTest {

    @Mock
    private ProfileSupportCases mockSupportCases;

    @Before
    public void setUp() throws Exception {
        reset(mockSupportCases);
    }

    @Test
    public void testSaveData() throws Exception {
        List<ProfileSupportCases.Case> openSupportCases = Collections.synchronizedList(new ArrayList<>());
        openSupportCases.add(new ProfileSupportCases.Case()
                .withId("id1")
                .withCreated("yyyy-mm-dd HH:MI:ss")
                .withStatus("status1")
                .withSubject("subject1")
                .withSubmittedBy("submittedby1"));
        openSupportCases.add(new ProfileSupportCases.Case()
                .withId("id2")
                .withCreated("yyyy-mm-dd HH:MI:ss")
                .withStatus("status2")
                .withSubject("subject2")
                .withSubmittedBy("submittedby2"));

        List<ProfileSupportCases.Case> resolvedSupportCases = Collections.synchronizedList(new ArrayList<>());
        resolvedSupportCases.add(new ProfileSupportCases.Case()
                .withId("id3")
                .withCreated("yyyy-mm-dd HH:MI:ss")
                .withStatus("status3")
                .withSubject("subject3")
                .withSubmittedBy("submittedby3"));


        when(mockSupportCases.getOpenCases()).thenReturn(openSupportCases);
        when(mockSupportCases.getResolvedCases()).thenReturn(resolvedSupportCases);

        // Calls the method
        SupportDynamoDBDao supportDAO = new SupportDynamoDBDao();
        boolean result = supportDAO.saveData("test-profile", mockSupportCases);

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
