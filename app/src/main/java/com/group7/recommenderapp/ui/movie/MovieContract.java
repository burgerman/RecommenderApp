package com.group7.recommenderapp.ui.movie;

import com.group7.recommenderapp.entities.MovieItem;
import java.util.List;

public interface MovieContract {
    interface View {
        void showMovies(List<MovieItem> movies);
        void showError(String message);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void loadMovies();
        void onMovieItemClicked(MovieItem movie);
        void addToFavorites(MovieItem movie);
        void removeFromFavorites(MovieItem movie);
    }
}
