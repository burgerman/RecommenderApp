package com.group7.recommenderapp.ui.signup;

import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;
    private UserService userService;

    public SignUpPresenter(SignUpContract.View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public void attemptSignUp(String username, String email, String password, String confirmPassword) {
        if (!validateInput(username, email, password, confirmPassword)) {
            return;
        }

        User newUser = userService.createNewUser(username, email, password);
        if (newUser != null) {
            view.showSignUpSuccess(newUser);
        } else {
            view.showSignUpFailure("Unable to create user");
        }
    }

    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            view.showValidationError("All fields are required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showValidationError("Invalid email format");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            view.showValidationError("Passwords do not match");
            return false;
        }
        if (password.length() < 6) {
            view.showValidationError("Password must be at least 6 characters long");
            return false;
        }
        return true;
    }
}
