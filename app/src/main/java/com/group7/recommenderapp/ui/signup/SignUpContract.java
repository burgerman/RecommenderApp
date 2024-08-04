package com.group7.recommenderapp.ui.signup;

public interface SignUpContract {
    interface View {
        void showSignUpSuccess();
        void showSignUpError(String message);
    }

    interface Presenter {
        void signUp(String usernameOrEmail, String password, String confirmPassword);
    }
}
