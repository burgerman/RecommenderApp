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

    public UserProfilePresenter(UserProfileContract.View view) {
        this.view = view;
        this.context = (Context) view;
    }

    @Override
    public void loadUserProfile() {
        String username = UserService.getUserServiceInstance(getApplicationContext()).getCurrentUser();
        User user = UserService.getUserServiceInstance(getApplicationContext()).selectUser(username);
        UserProfile userProfile = UserService.getUserServiceInstance(getApplicationContext()).selectUserProfile(username);
        user.setProfile(userProfile);
        if (userProfile != null) {
            view.displayUserProfile(userProfile);
        } else {
            view.showError("Failed to load user profile");
        }
    }

    public void updateUserProfile(Map<String, Object> userInfo) {
        // Prepare the user info map with additional genres
        prepareUserInfoWithAdditionalGenres(userInfo);

        UserService.getUserServiceInstance(getApplicationContext()).createOrUpdateUserProfile(userInfo);
        view.showProfileUpdateSuccess();
        loadUserProfile();

        // Download content files after updating profile
        downloadContentFile();
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


    private void downloadContentFile() {
        String movieContentFile = context.getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MOVIE_CONTENT_FILE_NAME;
        String musicContentFile = context.getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MUSIC_CONTENT_FILE_NAME;
        new FileUtil.DownloadFileTask().execute(FileUtil.MUSIC_CONTENT_FILE_URL, musicContentFile);
        new FileUtil.DownloadFileTask().execute(FileUtil.MOVIE_CONTENT_FILE_URL, movieContentFile);
    }
}
