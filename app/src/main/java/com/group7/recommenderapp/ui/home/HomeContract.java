package com.group7.recommenderapp.ui.home;

import com.group7.recommenderapp.entities.ContentItem;
import java.util.List;

public interface HomeContract {
    interface View {
        void showError(String message);
        void showLoading();
        void hideLoading();

        void showRecommendedMovieContent(List<ContentItem> content);
        void showRecommendedMusicContent(List<ContentItem> content);

        void showProgress();
        void hideProgress();
        void navigateToMovieTap();
        void navigateToMusicTap();
        void navigateToProfile();
    }

    interface Presenter {
        void loadRecommendedContent();
        void onHomeIconClicked();
        void onMovieIconClicked();
        void onMusicIconClicked();
        void onProfileIconClicked();
    }
}
