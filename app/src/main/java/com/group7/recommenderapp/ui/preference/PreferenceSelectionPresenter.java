package com.group7.recommenderapp.ui.preference;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.os.Bundle;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.GenreEntity;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.entities.UserPreference;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferenceSelectionPresenter implements PreferenceSelectionContract.Presenter {

    private PreferenceSelectionContract.View view;
    private UserProfileDao userProfileDao;
    private Context context;
    private RecommenderService movieRecommender;
    private RecommenderService musicRecommender;

    public PreferenceSelectionPresenter(PreferenceSelectionContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.userProfileDao = new UserProfileDao(DatabaseManager.getSharedInstance(context).getProfileCollection());
        try {
            movieRecommender = MovieRecommender.getInstance(context);
            musicRecommender = MusicRecommender.getInstance(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePreferences(String userId, List<String> moviePreferences, List<String> musicPreferences, Bundle additionalPreferences) {
        view.showLoading();

        UserService userService =  UserService.getUserServiceInstance(getApplicationContext());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", userService.getCurrentUser());
        userInfo.put("class1", "Movie");
        userInfo.put("class2", "Music");
        userInfo.put("language", "English");
        userInfo.put("age", 0);
        userInfo.put("gender", "");
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("categoriesClass1", moviePreferences);
        preferences.put("categoriesClass2", musicPreferences);
        preferences.put("additionalMoviePreferences", additionalPreferences.get("additionalMoviePreferences"));
        preferences.put("additionalMusicPreferences", additionalPreferences.get("additionalMusicPreferences"));
        preferences.put("LikedMovieList", null);
        preferences.put("LikedMusicList", null);
        userInfo.put("preferences", preferences);
        UserService.getUserServiceInstance(context).createOrUpdateUserProfile(userInfo);

        view.hideLoading();
        view.showPreferencesSaved();
    }

    @Override
    public void loadExistingPreferences() {
        view.showLoading();
        UserService us = UserService.getUserServiceInstance(context);

        UserProfile userProfile = us.selectUserProfile(us.getCurrentUser());
        if (userProfile != null && userProfile.getPreferences() != null) {
            List<String> moviePreferences = (List<String>) userProfile.getPreferences().getPreferenceDict().get("categoriesClass2");
            List<String> musicPreferences = (List<String>) userProfile.getPreferences().getPreferenceDict().get("categoriesClass1");

            // New: Get additional preferences
            List<String> additionalMoviePreferences = (List<String>) userProfile.getPreferences().getPreferenceDict().get("additionalMoviePreferences");
            List<String> additionalMusicPreferences = (List<String>) userProfile.getPreferences().getPreferenceDict().get("additionalMusicPreferences");

            String additionalMoviePreference = additionalMoviePreferences != null ? String.join(",", additionalMoviePreferences) : "";
            String additionalMusicPreference = additionalMusicPreferences != null ? String.join(",", additionalMusicPreferences) : "";

            view.displayExistingPreferences(moviePreferences, musicPreferences, additionalMoviePreference, additionalMusicPreference);
        }

        view.hideLoading();
    }

    @Override
    public List<String> getMovieGenres() {
        // Implement logic to get movie genres
//        return Arrays.asList("Action", "Comedy", "Drama", "Sci-Fi", "Thriller", "Horror");
        try {
            GenreEntity movieGenres = new GenreEntity();
            return movieGenres.getTopKGenres(10, movieRecommender.getGenresForRecommend());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getMusicGenres() {
        // Implement logic to get music genres
//        return Arrays.asList("Pop", "Rock", "Jazz", "Classical", "Hip-Hop", "Electronic");
        try {
            GenreEntity musicGenres = new GenreEntity();
            return musicGenres.getTopKGenres(10,  musicRecommender.getGenresForRecommend());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}