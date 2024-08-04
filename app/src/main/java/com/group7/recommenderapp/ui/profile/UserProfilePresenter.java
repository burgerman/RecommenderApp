package com.group7.recommenderapp.ui.profile;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.content.Intent;

import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.ui.login.LoginActivity;
import com.group7.recommenderapp.util.FileUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class UserProfilePresenter implements UserProfileContract.Presenter {

    private UserProfileContract.View view;
    private Context context;

    public UserProfilePresenter(UserProfileContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void loadUserProfile() {
        UserService us =  UserService.getUserServiceInstance(context);
        String username = us.getCurrentUser();
        User user = us.selectUser(username);
        UserProfile userProfile = us.selectUserProfile(username);
        user.setProfile(userProfile);
        if (userProfile != null) {
            view.displayUserProfile(userProfile);
        } else {
            view.showError("User Profile Not Found in DB");
        }
    }

    public void updateUserProfile(Map<String, Object> userInfo) {
        // Prepare the user info map with additional genres
        prepareUserInfoWithAdditionalGenres(userInfo);

        UserService.getUserServiceInstance(context).createOrUpdateUserProfile(userInfo);
        view.showProfileUpdateSuccess();
        loadUserProfile();
    }

    private void prepareUserInfoWithAdditionalGenres(Map<String, Object> userInfo) {
        Map<String, Object> preferences = (Map<String, Object>) userInfo.get("preferences");
        if (preferences == null) {
            preferences = new HashMap<>();
            userInfo.put("preferences", preferences);
        }

        // Handle additional movie genres
        String additionalMovieGenresString = (String) userInfo.get("additionalMovieGenres");
        if (additionalMovieGenresString != null && !additionalMovieGenresString.isEmpty()) {
            List<String> additionalMovieGenres = new ArrayList<>(Arrays.asList(additionalMovieGenresString.split(",")));
            preferences.put("additionalMovieGenres", additionalMovieGenres);
        }

        // Handle additional music genres
        String additionalMusicGenresString = (String) userInfo.get("additionalMusicGenres");
        if (additionalMusicGenresString != null && !additionalMusicGenresString.isEmpty()) {
            List<String> additionalMusicGenres = new ArrayList<>(Arrays.asList(additionalMusicGenresString.split(",")));
            preferences.put("additionalMusicGenres", additionalMusicGenres);
        }
    }

}
