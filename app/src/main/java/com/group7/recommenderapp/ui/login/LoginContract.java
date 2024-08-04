package com.group7.recommenderapp.ui.login;

public interface LoginContract {
    interface View {
        void showLoginSuccess();
        void showLoginError(String message);
    }

    interface Presenter {
        void login(String usernameOrEmail, String password);
    }
}
