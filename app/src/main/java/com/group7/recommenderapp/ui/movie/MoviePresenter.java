package com.group7.recommenderapp.ui.movie;

import android.content.Context;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.service.MovieConfig;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.RecommenderService;
import java.util.List;

public class MoviePresenter implements MovieContract.Presenter {
    private final MovieContract.View view;
    private final Context context;
    private final RecommenderService<MovieItem> movieRecommender;

    public MoviePresenter(MovieContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.movieRecommender = MovieRecommender.getInstance(context, new MovieConfig());
    }

    @Override
    public void loadMovies() {
        view.showLoading();
        movieRecommender.load();
        List<MovieItem> movies = movieRecommender.recommendByGenre(null); // Replace null with actual genres
        view.hideLoading();
        view.showMovies(movies);
    }

    @Override
    public void onMovieItemClicked(MovieItem movie) {
        // Handle movie item click, e.g., open movie details
    }

    @Override
    public void addToFavorites(MovieItem movie) {
        // Add movie to favorites
    }

    @Override
    public void removeFromFavorites(MovieItem movie) {
        // Remove movie from favorites
    }
}
