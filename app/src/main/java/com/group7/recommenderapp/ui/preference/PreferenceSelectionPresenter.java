package com.group7.recommenderapp.ui.preference;

import android.content.Context;
import com.group7.recommenderapp.util.DatabaseManager;
import java.util.Arrays;
import java.util.List;

public class PreferenceSelectionPresenter implements PreferenceSelectionContract.Presenter {
    private final PreferenceSelectionContract.View view;
    private final Context context;

    public PreferenceSelectionPresenter(PreferenceSelectionContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void savePreferences(List<String> moviePreferences, List<String> musicPreferences) {
        if (moviePreferences.isEmpty() || musicPreferences.isEmpty()) {
            view.showError("Please select at least one preference in each category");
            return;
        }

        view.showLoading();

        DatabaseManager dbMgr = DatabaseManager.getSharedInstance(context);
        dbMgr.saveMoviePreferences(moviePreferences);
        dbMgr.saveMusicPreferences(musicPreferences);

        view.hideLoading();
        view.showPreferencesSaved();
        view.navigateToHome();
    }

    @Override
    public List<String> getMovieGenres() {
        // TODO: Fetch this from a proper data source
        return Arrays.asList("Action", "Comedy", "Drama", "Sci-Fi", "Horror", "Romance", "Thriller");
    }

    @Override
    public List<String> getMusicGenres() {
        // TODO: Fetch this from a proper data source
        return Arrays.asList("Rock", "Pop", "Hip-Hop", "Jazz", "Classical", "Electronic", "Country");
    }
}
