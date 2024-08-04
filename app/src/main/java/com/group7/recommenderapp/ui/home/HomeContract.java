package com.group7.recommenderapp.ui.home;

import com.group7.recommenderapp.entities.ContentItem;
import java.util.List;

public interface HomeContract {
    interface View {

        void showRecommendedMovieContent(List<ContentItem> content);
        void showRecommendedMusicContent(List<ContentItem> content);
        void navigateToMovieTap();
        void navigateToMusicTap();
        void navigateToProfile();
        void navigateToHelp();
    }

    interface Presenter {
        void loadMovieContent();
        void loadMusicContent();
        void onMovieIconClicked();
        void onMusicIconClicked();
        void onProfileIconClicked();
        void onHelpIconClicked();
    }
}
