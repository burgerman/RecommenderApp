package com.group7.recommenderapp.ui.movie;

import com.group7.recommenderapp.entities.MovieItem;
import java.util.List;

public interface MovieContract {
    interface View {
        void showMovies(List<MovieItem> movies);
        void showError(String message);

        void showMessage(String message);

        void showLoading();
        void hideLoading();
        void updateHeartButton(boolean isFilled);
        void showFeedbackSubmitted();
        void showMovieDetails(MovieItem movie);
    }

    interface Presenter {
        void loadMovies();
        void onMovieItemClicked(MovieItem movie);
        void onHeartButtonClicked();
        void onFeedbackSubmitted(String feedback);
    }
}
