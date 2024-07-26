package com.group7.recommenderapp.ui.login;

import android.content.Context;
import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.util.DatabaseManager;
import com.group7.recommenderapp.util.UserUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View view;
    private final Context context;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Username or password cannot be empty");
            return;
        }

        DatabaseManager dbMgr = DatabaseManager.getSharedInstance(context);
        dbMgr.openOrCreateDatabaseForUser(context, username);

        UserDao userDao = new UserDao(dbMgr.getUserCollection());
        User user = userDao.getUser(UserUtils.generateUserDocId(username));

        if (user != null && user.getPassword().equals(password)) {
            view.navigateToUserProfile();
        } else {
            view.showError("Invalid username or password");
        }
    }

    @Override
    public void handleSignUp(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("All fields are required for sign up");
            return;
        }

        DatabaseManager dbMgr = DatabaseManager.getSharedInstance(context);
        dbMgr.openOrCreateDatabaseForUser(context, username);

        UserDao userDao = new UserDao(dbMgr.getUserCollection());
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setDocumentId(UserUtils.generateUserDocId(username));

        if (userDao.createOrUpdateUser(user)) {
            view.navigateToPreferenceSelection();
        } else {
            view.showError("Sign up failed");
        }
    }
}
