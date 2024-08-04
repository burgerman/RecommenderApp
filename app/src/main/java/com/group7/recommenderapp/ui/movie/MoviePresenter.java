package com.group7.recommenderapp.ui.movie;

import android.content.Context;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.entities.ResultSet;
import com.group7.recommenderapp.service.MovieConfig;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.RecommenderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoviePresenter implements MovieContract.Presenter {
    private final MovieContract.View view;
    private final Context context;
    private final RecommenderService<ContentItem> movieRecommender;

    public MoviePresenter(MovieContract.View view, Context context) {
        this.view = view;
        this.context = context;
        try {
            this.movieRecommender = MovieRecommender.getInstance(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadMovies() {
        view.showLoading();
        movieRecommender.load();
        List<ContentItem> movies = movieRecommender.recommendByGenre(new ArrayList<>()); // Ensure genres is not null
        ResultSet rs = new ResultSet();
        rs.setItemset(movies);
        view.hideLoading();
        view.showMovies(rs.getTopK(20));
    }

    @Override
    public void onMovieItemClicked(ContentItem movie) {
        // Handle movie item click, e.g., open movie details
    }

    @Override
    public void onHeartButtonClicked() {

    }

    @Override
    public void onFeedbackSubmitted(String feedback) {

    }
}
