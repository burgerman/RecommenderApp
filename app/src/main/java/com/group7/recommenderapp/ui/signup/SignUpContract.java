package com.group7.recommenderapp.ui.signup;

import com.group7.recommenderapp.entities.User;

public interface SignUpContract {
    interface View {
        void showSignUpSuccess(User newUser);
        void showSignUpFailure(String message);
        void showValidationError(String message);
    }

    interface Presenter {
        void attemptSignUp(String username, String email, String password, String confirmPassword);
    }
}
