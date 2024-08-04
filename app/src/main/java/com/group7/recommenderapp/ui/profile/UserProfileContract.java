package com.group7.recommenderapp.ui.profile;

import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;

import java.util.Map;

public interface UserProfileContract {
    interface View {
        void displayUserProfile(UserProfile userProfile);
        void showError(String message);
        void navigateToLogin();
        void showProfileUpdateSuccess();
    }

    interface Presenter {
        void loadUserProfile();
        void updateUserProfile(Map<String, Object> userInfo);
    }
}
