package com.group7.recommenderapp.ui.preference;

import android.content.Context;
import android.util.Log;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.entities.UserPreference;
import java.util.Arrays;
import java.util.List;

public class PreferenceSelectionPresenter implements PreferenceSelectionContract.Presenter {
    private final PreferenceSelectionContract.View view;
    private final Context context;
    private final DatabaseManager dbManager;
    private final UserProfileDao userProfileDao;

    public PreferenceSelectionPresenter(PreferenceSelectionContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.dbManager = DatabaseManager.getSharedInstance(context);
        this.userProfileDao = new UserProfileDao(dbManager.getProfileCollection());
    }

    @Override
    public void savePreferences(String userId, List<String> moviePreferences, List<String> musicPreferences, String additionalPreference) {
        if (moviePreferences.isEmpty() || musicPreferences.isEmpty()) {
            view.showError("Please select at least one preference in each category");
            return;
        }

        view.showLoading();

        try {
            UserProfile userProfile = userProfileDao.getUserProfile(userId);
            if (userProfile == null) {
                userProfile = new UserProfile(userId);
            }

            UserPreference preferences = new UserPreference();
            preferences.setCategoriesClass1(moviePreferences);
            preferences.setCategoriesClass2(musicPreferences);
            preferences.setAdditionalPreference(additionalPreference);

            userProfile.setPreferences(preferences);
            userProfileDao.createOrUpdateProfile(userProfile);

            view.hideLoading();
            view.showPreferencesSaved();
        } catch (Exception e) {
            Log.e("PreferencePresenter", "Error saving preferences", e);
            view.hideLoading();
            view.showError("An error occurred while saving preferences. Please try again.");
        }
    }

    @Override
    public List<String> getMovieGenres() {
        return Arrays.asList("Action", "Comedy", "Drama", "Sci-Fi", "Horror", "Romance", "Thriller");
    }

    @Override
    public List<String> getMusicGenres() {
        return Arrays.asList("Rock", "Pop", "Hip-Hop", "Jazz", "Classical", "Electronic", "Country");
    }

    @Override
    public void loadExistingPreferences(String userId) {
        try {
            UserProfile userProfile = userProfileDao.getUserProfile(userId);
            if (userProfile != null && userProfile.getPreferences() != null) {
                UserPreference preferences = userProfile.getPreferences();
                view.displayExistingPreferences(
                        preferences.getCategoriesClass1(),
                        preferences.getCategoriesClass2(),
                        preferences.getAdditionalPreference()
                );
            }
        } catch (Exception e) {
            Log.e("PreferencePresenter", "Error loading existing preferences", e);
            view.showError("Error loading existing preferences: " + e.getMessage());
        }
    }
}
