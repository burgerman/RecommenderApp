package com.group7.recommenderapp.service;

import android.content.Context;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.util.UserUtils;

import java.util.Map;

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

    public int createNewUser(String username, String password) {
        User user = new User();
        if(UserUtils.isValidEmail(username)) {
            user.setUserName(username);
            user.setPassword(password);
            user.setDocumentId(UserUtils.generateUserDocId(username));
            userDao.createOrUpdateUser(user);
        }
        return 400;
    }

    public int authenUser(String username, String password) {
        String userId = UserUtils.generateUserDocId(username);
        User user = userDao.getUser(userId);
        if(user !=null && user.getPassword().equals(password)) {
            return 200;
        }
        return 400;
    }

    public void createUserProfile(Map<String, Object> userInfo) {

    }

    public void updateUserProfile(Map<String, Object> userInfo) {

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
