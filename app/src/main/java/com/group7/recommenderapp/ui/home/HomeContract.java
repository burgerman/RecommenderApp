package com.group7.recommenderapp.ui.home;

import com.group7.recommenderapp.entities.ContentItem;
import java.util.List;

public interface HomeContract {
    interface View {
        void showRecommendedContent(List<ContentItem> content);
        void showError(String message);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void loadRecommendedContent();
    }
}
