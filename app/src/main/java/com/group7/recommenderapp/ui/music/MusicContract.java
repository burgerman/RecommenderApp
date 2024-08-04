package com.group7.recommenderapp.ui.music;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import java.util.List;

public interface MusicContract {
    interface View {
        void showMusic(List<ContentItem> music);
        void showError(String message);
        void showLoading();
        void hideLoading();
        void updateHeartButton(boolean isFilled);
        void showFeedbackSubmitted();
        void showMusicDetails(MusicItem music);
    }

    interface Presenter {
        void loadMusic();
        void onMusicItemClicked(ContentItem music);
        void onHeartButtonClicked();
        void onFeedbackSubmitted(String feedback);
    }
}
