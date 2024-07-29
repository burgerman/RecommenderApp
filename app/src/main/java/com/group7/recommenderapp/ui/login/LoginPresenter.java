package com.group7.recommenderapp.ui.login;

import com.couchbase.lite.Collection;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import android.util.Log;

public class LoginPresenter {
    private static final String TAG = "LoginPresenter";
    private LoginActivity loginActivity;
    private UserDao userDao;

    public LoginPresenter(LoginActivity loginActivity, Collection userCollection) {
        this.loginActivity = loginActivity;
        this.userDao = new UserDao(userCollection);
    }

    public void handleLogin(String usernameOrEmail, String password) {
        Log.d(TAG, "Attempting login for: " + usernameOrEmail);
        User user = userDao.getUserByUsername(usernameOrEmail);
        if (user != null && user.getPassword().equals(password)) {
            Log.d(TAG, "Login successful for user: " + usernameOrEmail);
            loginActivity.onLoginSuccess(user);
        } else {
            Log.d(TAG, "Login failed for user: " + usernameOrEmail);
            loginActivity.onLoginFailure();
        }
    }
}
