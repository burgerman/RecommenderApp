package com.group7.recommenderapp.ui.home;

import android.content.Context;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.service.MovieConfig;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.MusicConfig;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.service.MusicRecommender;

import java.io.IOException;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final Context context;
    private final RecommenderService<ContentItem> movieRecommenderService;
    private final RecommenderService<ContentItem> musicRecommenderService;

    public HomePresenter(HomeContract.View view, Context context) {
        this.view = view;
        this.context = context;
        try {
            this.movieRecommenderService = MovieRecommender.getInstance(context);
            this.musicRecommenderService = MusicRecommender.getInstance(context); // Assuming similar config for music
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadRecommendedContent() {
        view.showLoading();

        // Load movie recommendations
        List<ContentItem> recommendedMovies = movieRecommenderService.recommendByGenre(null); // Replace null with actual genres
        view.showRecommendedContent(recommendedMovies);

        // Load music recommendations
        List<ContentItem> recommendedMusic = musicRecommenderService.recommendByGenre(null); // Replace null with actual genres
        view.showRecommendedContent(recommendedMusic);

        view.hideLoading();
    }

    @Override
    public void onContentItemClicked(ContentItem item) {
        // Handle content item click, e.g., open content details
    }
}
