package com.group7.recommenderapp;


import com.group7.recommenderapp.util.UserUtils;
import org.junit.Assert;
import org.junit.Test;

public class DocIDsUnitTest {
    private String userName;
    private String userDocID;
    private String profileDocID;

    @Test
    public void testProfileDocIDGeneration() {
        if(userName==null) {
            userName = "menghao@mylaurier.ca";
            userDocID = UserUtils.generateUserDocId(userName);
        }
        profileDocID = UserUtils.generateUserProfileDocId(userName, userDocID);
        System.out.println("profile doc id: "+profileDocID);
        Assert.assertNotNull(profileDocID);
        Assert.assertEquals(profileDocID, UserUtils.generateUserProfileDocId("menghao@mylaurier.ca", UserUtils.generateUserDocId("menghao@mylaurier.ca")));
    }
    @Test
    public void testUserDocIDGeneration() {
        userName = "menghao@mylaurier.ca";
        userDocID = UserUtils.generateUserDocId(userName);
        System.out.println("user doc id: "+userDocID);
        Assert.assertNotNull(userDocID);
        Assert.assertEquals(userDocID, UserUtils.generateUserDocId("menghao@mylaurier.ca"));
    }
}