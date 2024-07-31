package com.group7.recommenderapp.service;

import android.content.Context;
import android.util.Log;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserPreference;
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
        if(UserUtils.isValidEmail(username)) {
            int res = authenUser(username, password);
            if(res == 400) {
                // user not found in DB
                User user = new User();
                user.setUserName(username);
                user.setPassword(password);
                user.setDocumentId(UserUtils.generateUserDocId(username));
                if (userDao.createOrUpdateUser(user)) return 200;
            } else if (res == 200) {
                // user exists, auth passed
                return 200;
            } else {
                // user exists, password not correct
                return 300;
            }
        }
        return 400;
    }

    public int authenUser(String username, String password) {
        User user = selectUser(username);
        if(user !=null) {
            if(UserUtils.isCorrectPassword(user, password)){
                return 200;
            }
            return 300;
        }
        return 400;
    }

    public void createOrUpdateUserProfile(Map<String, Object> userInfo) {
        if(userInfo.get("username")!=null) {
            String userName = (String) userInfo.get("username");
            String profileDocId = UserUtils.getUserProfileIDByName(userName);
            UserProfile userProfile = new UserProfile(profileDocId);
            userProfile.setAge(userInfo.get("age")!= null? (Integer) userInfo.get("age"):0);
            userProfile.setGender(userInfo.get("gender")!=null? (String) userInfo.get("gender"):"");
            UserPreference userPreference = new UserPreference((String) userInfo.get("class1"));
            userPreference.setPreferredLanguage(userInfo.get("language")!=null?(String) userInfo.get("language"):"");
            userPreference.setClass2(userInfo.get("class2")!=null?(String) userInfo.get("class2"):"");
            userPreference.setPreferenceDict(userInfo.get("preferences")!=null?(Map<String, Object>) userInfo.get("preferences"):null);
            userProfile.setPreferences(userPreference);
            userProfileDao.createOrUpdateProfile(userProfile);
        } else {
            Log.i("UserService createOrUpdateUserProfile", "User Name Not Found");
        }

    }

    public User selectUser(String username) {
        if(UserUtils.isValidEmail(username)) {
            String docId = UserUtils.generateUserDocId(username);
            return userDao.getUser(docId);
        }
        return null;
    }

    public void deleteUser(String username) {
        if(userDao.deleteUserDoc(UserUtils.generateUserDocId(username))){
            Log.i("UserService", "User: "+username + " has been deleted from the DB");
        }
    }

    public UserProfile selectUserProfile(String username) {
        if(UserUtils.isValidEmail(username)) {
            String profileDocId = UserUtils.getUserProfileIDByName(username);
            return userProfileDao.getUserProfile(profileDocId);
        }
        return null;
    }

    public void deleteUserProfile(String username) {
        String profileDocId = UserUtils.getUserProfileIDByName(username);
        if(userProfileDao.deleteProfileDocument(profileDocId)){
            Log.i("UserService", "The profile of User: "+username + " has been deleted from the DB");
        }
    }
}
