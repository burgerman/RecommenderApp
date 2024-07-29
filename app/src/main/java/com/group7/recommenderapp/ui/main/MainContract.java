package com.group7.recommenderapp.ui.main;

import com.group7.recommenderapp.entities.User;

public interface MainContract {
    interface View {
        void showUserInfo(User user);
        void showError(String message);
        void showSuccess(String message);
    }

    interface Presenter {
        void start();
        void createTestUser();
    }
}
