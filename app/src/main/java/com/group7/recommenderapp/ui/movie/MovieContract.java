package com.group7.recommenderapp.ui.movie;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import java.util.List;

public interface MovieContract {
    interface View {
//        void showMovies(List<ContentItem> movies);
        void showError(String message);

        void showMessage(String message);

        void showLoading();
        void hideLoading();
//        void updateHeartButton(boolean isFilled);
        void showFeedbackSubmitted();
//        void showMovieDetails(MovieItem movie);
        void navigateToHomeTap();
        void navigateToMusicTap();
        void navigateToProfile();
    }

    interface Presenter {
        void loadMovies();
        void onMovieItemClicked(ContentItem movie);
//        void onHeartButtonClicked();
        void onFeedbackSubmitted(String feedback);
        void onHomeIconClicked();
        void onMusicIconClicked();
        void onProfileIconClicked();
    }
}
