package com.group7.recommenderapp.ui.login;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;

import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.util.FileUtil;
import com.group7.recommenderapp.util.UserUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private Context context;

    public LoginPresenter(LoginContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void login(String usernameOrEmail, String password) {
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            view.showLoginError("Please fill in all fields");
            return;
        }
        UserService us =  UserService.getUserServiceInstance(context);
        int res = us.authenUser(usernameOrEmail, password);
        if (res == 200) {
            us.setCurrentUser(usernameOrEmail);
            boolean newUser;
            newUser = us.selectUserProfile(usernameOrEmail) == null;
            // Call to download content files before calling recommendation services
            downloadContentFile();
            view.showLoginSuccess(newUser);
        } else if (res == 300) {
            view.showLoginError("Wrong password");
        } else {
            view.showLoginError("User not found");
        }
    }

    private void downloadContentFile() {
        String movieContentFile = context.getApplicationContext().getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MOVIE_CONTENT_FILE_NAME;
        String musicContentFile = context.getApplicationContext().getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MUSIC_CONTENT_FILE_NAME;
        new FileUtil.DownloadFileTask().execute(FileUtil.MUSIC_CONTENT_FILE_URL, musicContentFile);
        new FileUtil.DownloadFileTask().execute(FileUtil.MOVIE_CONTENT_FILE_URL, movieContentFile);
    }
}
