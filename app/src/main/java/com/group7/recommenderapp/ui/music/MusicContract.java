package com.group7.recommenderapp.ui.music;

import com.group7.recommenderapp.entities.MusicItem;
import java.util.List;

public interface MusicContract {
    interface View {
        void showMusic(List<MusicItem> music);
        void showError(String message);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void loadMusic();
        void onMusicItemClicked(MusicItem music);
        void addToFavorites(MusicItem music);
        void removeFromFavorites(MusicItem music);
    }
}
