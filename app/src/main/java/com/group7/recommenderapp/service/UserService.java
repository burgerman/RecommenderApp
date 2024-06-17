package com.group7.recommenderapp.service;

import android.content.Context;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.util.UserUtils;

public class UserService {

    private UserDao userDao;

    private UserProfileDao userProfileDao;

    private static UserService userServiceInstance = null;

    protected UserService(Context context) {
        this.userDao = new UserDao(DatabaseManager.getSharedInstance(context).getUserCollection());
        this.userProfileDao = new UserProfileDao(DatabaseManager.getSharedInstance(context).getProfileCollection());
    }

    public static UserService getUserServiceInstance(Context context) {
        if (userServiceInstance == null) {
            synchronized (UserService.class) {
                if(userServiceInstance == null) {
                    userServiceInstance = new UserService(context);
                }
            }
        }
        return userServiceInstance;
    }

    public void createNewUser(User user) {

    }

    public void updateUserInfo(User user) {

    }

    public void createUserProfile(UserProfile userProfile) {

    }

    public void updateUserProfile(User user) {

    }

    public User selectUser(String username) {
        if(UserUtils.isValidEmail(username)) {
            String docId = UserUtils.generateUserDocId(username);
            return userDao.getUser(docId);
        }
        return null;
    }

    public UserProfile selectUserProfile(String username) {
        if(UserUtils.isValidEmail(username)) {
            String userDocId = UserUtils.generateUserDocId(username);
            String profileDocId = UserUtils.generateUserProfileDocId(username, userDocId);
            return userProfileDao.getUserProfile(profileDocId);
        }
        return null;
    }
}
