package com.group7.recommenderapp.ui.preference;

import java.util.List;

public interface PreferenceSelectionContract {
    interface View {
        void showPreferencesSaved();
        void showError(String message);
        void navigateToHome();
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void savePreferences(List<String> moviePreferences, List<String> musicPreferences);
        List<String> getMovieGenres();
        List<String> getMusicGenres();
    }
}
