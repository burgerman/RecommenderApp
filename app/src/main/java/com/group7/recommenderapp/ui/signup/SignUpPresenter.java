package com.group7.recommenderapp.ui.signup;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.util.UserUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;
    private UserDao userDao;

    public SignUpPresenter(SignUpContract.View view, UserDao userDao) {
        this.view = view;
        this.userDao = userDao;
    }

    @Override
    public void signUp(String usernameOrEmail, String password, String confirmPassword) {
        if (usernameOrEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            view.showSignUpError("Please fill in all fields");
            return;
        }

        if (!UserUtils.isValidEmail(usernameOrEmail)) {
            view.showSignUpError("Invalid username or email");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.showSignUpError("Passwords do not match");
            return;
        }

        int res = UserService.getUserServiceInstance(getApplicationContext()).createNewUser(usernameOrEmail, password);

        if (res == 200) {
            view.showSignUpSuccess();
        } else {
            view.showSignUpError("Sign up failed");
        }
    }
}