package com.group7.recommenderapp.ui.music;

import android.content.Context;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.service.MusicConfig;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.RecommenderService;

import java.io.IOException;
import java.util.List;

public class MusicPresenter implements MusicContract.Presenter {
    private final MusicContract.View view;
    private final Context context;
    private final RecommenderService<MusicItem> musicRecommender;

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
        List<MusicItem> music = musicRecommender.recommendByGenre(null); // Replace null with actual genres
        view.hideLoading();
        view.showMusic(music);
    }

    @Override
    public void onMusicItemClicked(MusicItem music) {
        // Handle music item click, e.g., open music details
    }

    @Override
    public void onHeartButtonClicked() {

    }

    @Override
    public void onFeedbackSubmitted(String feedback) {

    }

}
