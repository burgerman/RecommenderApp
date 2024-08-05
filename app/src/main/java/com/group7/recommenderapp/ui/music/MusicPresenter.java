package com.group7.recommenderapp.ui.music;

import android.content.Context;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.entities.ResultSet;
import com.group7.recommenderapp.service.MusicConfig;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.RecommenderService;

import java.io.IOException;
import java.util.List;

public class MusicPresenter implements MusicContract.Presenter {
    private final MusicContract.View view;
    private final Context context;
    private final RecommenderService<ContentItem> musicRecommender;

    public MusicPresenter(MusicContract.View view, Context context) {
        this.view = view;
        this.context = context;
        // Initialize MusicRecommender
//        MusicConfig config = new MusicConfig();
        try {
            this.musicRecommender = MusicRecommender.getInstance(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadMusic() {
        view.showLoading();
        musicRecommender.load();
        List<ContentItem> musics = musicRecommender.recommendByGenre(null); // Replace null with actual genres
        ResultSet rs = new ResultSet();
        rs.setItemset(musics);
        view.hideLoading();
        view.showMusic(rs.getTopK(20));
    }

    @Override
    public void onMusicItemClicked(ContentItem music) {
        // Handle music item click, e.g., open music details
    }

//    @Override
//    public void onHeartButtonClicked() {
//
//    }

    @Override
    public void onFeedbackSubmitted(String feedback) {

    }
    @Override
    public void onHomeIconClicked() {
        view.navigateToHomeTap();
    }

    @Override
    public void onMovieIconClicked() {
        view.navigateToMovieTap();
    }

    @Override
    public void onProfileIconClicked() {
        view.navigateToProfile();
    }
}
