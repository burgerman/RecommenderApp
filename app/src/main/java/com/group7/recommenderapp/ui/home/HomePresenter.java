package com.group7.recommenderapp.ui.home;

import android.content.Context;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.GenreEntity;
import com.group7.recommenderapp.entities.ResultSet;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.service.MovieConfig;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.MusicConfig;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.UserService;

import java.io.IOException;
import java.util.Arrays;
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
    public void loadMovieContent() {
        // Load movie recommendations
        movieRecommenderService.load();
        UserService us = UserService.getUserServiceInstance(context);
        UserProfile up = us.selectUserProfile(us.getCurrentUser());
        List<String> genres = (List<String>) up.getPreferences().getPreferenceDict().get("categoriesClass1");
        List<ContentItem> recommendedMovies = movieRecommenderService.recommendByGenre(genres!=null?genres: Arrays.asList("Action", "Comedy"));
        ResultSet movieResultSet = new ResultSet();
        movieResultSet.setItemset(recommendedMovies);
        view.showRecommendedMovieContent(movieResultSet.getTopK(10));
    }

    @Override
    public void loadMusicContent() {
        // Load music recommendations
        musicRecommenderService.load();
        UserService us = UserService.getUserServiceInstance(context);
        UserProfile up = us.selectUserProfile(us.getCurrentUser());
        List<String> genres = (List<String>) up.getPreferences().getPreferenceDict().get("categoriesClass2");
        List<ContentItem> recommendedMusic = musicRecommenderService.recommendByGenre(genres!=null?genres: Arrays.asList("Rock", "Pop"));
        ResultSet musicResultSet = new ResultSet();
        musicResultSet.setItemset(recommendedMusic);
        view.showRecommendedMusicContent(musicResultSet.getTopK(10));
    }

    @Override
    public void onMovieIconClicked() {
        view.navigateToMovieTap();
    }

    @Override
    public void onMusicIconClicked() {
        view.navigateToMusicTap();
    }

    @Override
    public void onProfileIconClicked() {
        view.navigateToProfile();
    }

    @Override
    public void onHelpIconClicked() { view.navigateToHelp();}
}
