package com.group7.recommenderapp.service;

import android.content.Context;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.entities.UserPreference;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.util.UserUtils;
import java.util.Map;
import java.util.List;

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
                if (userServiceInstance == null) {
                    userServiceInstance = new UserService(context);
                }
            }
        }
        return userServiceInstance;
    }

    public User createNewUser(String username, String email, String password) {
        if (UserUtils.isValidEmail(email)) {
            User user = new User(username, email, password);
            user.setDocumentId(UserUtils.generateUserDocId(email));
            if (userDao.createOrUpdateUser(user)) {
                return user;
            }
        }
        return null;
    }

    public int authenUser(String username, String password) {
        String userId = UserUtils.generateUserDocId(username);
        User user = userDao.getUser(userId);
        if (user != null && user.getPassword().equals(password)) {
            return 200;
        }
        return 400;
    }

    public void createUserProfile(Map<String, Object> userInfo) {
        UserProfile userProfile = new UserProfile((String) userInfo.get("userDocumentId"));
        userProfile.setGender((String) userInfo.get("gender"));
        userProfile.setAge((Integer) userInfo.get("age"));
        UserPreference preferences = new UserPreference((String) userInfo.get("class1"));
        preferences.setClass2((String) userInfo.get("class2"));
        preferences.setPreferredLanguage((String) userInfo.get("preferredLanguage"));
        updatePreferences(preferences, (Map<String, Object>) userInfo.get("preferences"));
        userProfile.setPreferences(preferences);
        userProfileDao.createOrUpdateProfile(userProfile);
    }

    private void updatePreferences(UserPreference preferences, Map<String, Object> preferencesMap) {
        if (preferencesMap != null) {
            preferences.setCategoriesClass1((List<String>) preferencesMap.get("categoriesClass1"));
            preferences.setCategoriesClass2((List<String>) preferencesMap.get("categoriesClass2"));
            preferences.setLikedMovieList((List<String>) preferencesMap.get("LikedMovieList"));
            preferences.setLikedMusicList((List<String>) preferencesMap.get("LikedMusicList"));
        }
    }

    public void updateUserProfile(Map<String, Object> userInfo) {
        createUserProfile(userInfo);
    }

    public User selectUser(String username) {
        if (UserUtils.isValidEmail(username)) {
            String docId = UserUtils.generateUserDocId(username);
            return userDao.getUser(docId);
        }
        return null;
    }

    public UserProfile selectUserProfile(String username) {
        if (UserUtils.isValidEmail(username)) {
            String userDocId = UserUtils.generateUserDocId(username);
            String profileDocId = UserUtils.generateUserProfileDocId(username, userDocId);
            return userProfileDao.getUserProfile(profileDocId);
        }
        return null;
    }
}
