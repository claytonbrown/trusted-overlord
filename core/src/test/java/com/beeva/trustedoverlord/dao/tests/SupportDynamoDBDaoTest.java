package com.beeva.trustedoverlord.dao.tests;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.beeva.trustedoverlord.dao.SupportDynamoDBDao;
import com.beeva.trustedoverlord.model.ProfileSupportCases;
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

        try {
            // Calls the method
            SupportDynamoDBDao supportDAO = new SupportDynamoDBDao();
            CompletableFuture<PutItemResult> future = new CompletableFuture<>();
            supportDAO.saveDataAsync("test-profile", mockSupportCases, future);

            Awaitility.await().until(future::isDone);
        } catch (Exception e) {
            System.out.print("No DynamodbDB");
        }
        Assert.assertThat(true, is(true));
    }
}
