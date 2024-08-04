package com.group7.recommenderapp.ui.signup;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;

import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.util.UserUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;
    private Context context;

    public SignUpPresenter(SignUpContract.View view, Context context) {
        this.view = view;
        this.context = context;
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

        int res = UserService.getUserServiceInstance(context).createNewUser(usernameOrEmail, password);

        if (res == 200) {
            view.showSignUpSuccess();
        } else {
            view.showSignUpError("Sign up failed");
        }
    }
}