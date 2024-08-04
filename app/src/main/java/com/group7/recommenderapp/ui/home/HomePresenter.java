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
        UserService us = UserService.getUserServiceInstance(context);
        UserProfile up = us.selectUserProfile(us.getCurrentUser());
        List<String> genres = (List<String>) up.getPreferences().getPreferenceDict().get("categoriesClass1");
        List<ContentItem> recommendedMovies = movieRecommenderService.recommendByGenre(genres!=null?genres: Arrays.asList("Action", "Comedy"));
//        ContentItem item1 = new ContentItem();
//        item1.setScore(5);
//        item1.setGenres(Arrays.asList("Comedy", "Action"));
//        item1.setTitle("movie 1");
//        item1.setId(11);
//        item1.setConfidence(90.0f);
//        item1.setSelected(false);
//        ContentItem item2 = new ContentItem();
//        item1.setScore(4);
//        item1.setGenres(Arrays.asList("Comedy", "Action"));
//        item1.setTitle("movie 2");
//        item1.setId(18);
//        item1.setConfidence(96.0f);
//        item1.setSelected(false);
//        ContentItem item3 = new ContentItem();
//        item1.setScore(3);
//        item1.setGenres(Arrays.asList("Comedy", "Action"));
//        item1.setTitle("movie 3");
//        item1.setId(12);
//        item1.setConfidence(80.0f);
//        item1.setSelected(false);
//        ContentItem item4 = new ContentItem();
//        item1.setScore(2);
//        item1.setGenres(Arrays.asList("Comedy", "Action"));
//        item1.setTitle("movie 4");
//        item1.setId(28);
//        item1.setConfidence(70.0f);
//        item1.setSelected(false);
//        ResultSet movieResultSet = new ResultSet();
//        movieResultSet.setItemset(Arrays.asList(item1, item2,item3,item4));
        ResultSet movieResultSet = new ResultSet();
        movieResultSet.setItemset(recommendedMovies);
        view.showRecommendedMovieContent(movieResultSet.getTopK(4));
    }

    @Override
    public void loadMusicContent() {
        // Load music recommendations
        UserService us = UserService.getUserServiceInstance(context);
        UserProfile up = us.selectUserProfile(us.getCurrentUser());
        List<String> genres = (List<String>) up.getPreferences().getPreferenceDict().get("categoriesClass2");
        List<ContentItem> recommendedMusic = musicRecommenderService.recommendByGenre(genres!=null?genres: Arrays.asList("Rock", "Pop"));
//        ContentItem item1 = new ContentItem();
//        item1.setScore(5);
//        item1.setGenres(Arrays.asList("Pop", "Rock"));
//        item1.setTitle("music 1");
//        item1.setId(21);
//        item1.setConfidence(90.0f);
//        item1.setSelected(false);
//        ContentItem item2 = new ContentItem();
//        item1.setScore(4);
//        item1.setGenres(Arrays.asList("Pop", "Rock"));
//        item1.setTitle("music 2");
//        item1.setId(19);
//        item1.setConfidence(96.0f);
//        item1.setSelected(false);
//        ContentItem item3 = new ContentItem();
//        item1.setScore(3);
//        item1.setGenres(Arrays.asList("Pop", "Rock"));
//        item1.setTitle("music 3");
//        item1.setId(40);
//        item1.setConfidence(80.0f);
//        item1.setSelected(false);
//        ContentItem item4 = new ContentItem();
//        item1.setScore(2);
//        item1.setGenres(Arrays.asList("Pop", "Rock"));
//        item1.setTitle("music 4");
//        item1.setId(30);
//        item1.setConfidence(70.0f);
//        item1.setSelected(false);
//        ResultSet musicResultSet = new ResultSet();
//        musicResultSet.setItemset(Arrays.asList(item1, item2, item3, item4));
        ResultSet musicResultSet = new ResultSet();
        musicResultSet.setItemset(recommendedMusic);
        view.showRecommendedMusicContent(musicResultSet.getTopK(4));
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
