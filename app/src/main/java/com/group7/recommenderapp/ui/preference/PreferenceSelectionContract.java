package com.group7.recommenderapp.ui.preference;

import android.os.Bundle;
import java.util.List;

public interface PreferenceSelectionContract {
    interface View {
        void showPreferencesSaved();
        void showError(String message);
        void navigateToHome();
        void showLoading();
        void hideLoading();
        void displayExistingPreferences(List<String> moviePreferences, List<String> musicPreferences, String additionalMoviePreference, String additionalMusicPreference);
    }

    interface Presenter {
        void savePreferences(String userId, List<String> moviePreferences, List<String> musicPreferences, List<String> addiMoviePref, List<String> addMusicPref);
        void loadExistingPreferences();
        List<String> getMovieGenres();
        List<String> getMusicGenres();
    }
}
