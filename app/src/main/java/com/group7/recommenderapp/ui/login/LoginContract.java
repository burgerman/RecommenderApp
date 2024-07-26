package com.group7.recommenderapp.ui.login;

public interface LoginContract {
    interface View {
        void showError(String message);
        void navigateToUserProfile();
        void navigateToPreferenceSelection();
    }

    interface Presenter {
        void handleLogin(String username, String password);
        void handleSignUp(String username, String password);
    }
}
