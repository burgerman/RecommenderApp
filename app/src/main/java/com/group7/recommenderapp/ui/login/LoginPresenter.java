package com.group7.recommenderapp.ui.login;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.util.FileUtil;
import com.group7.recommenderapp.util.UserUtils;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void login(String usernameOrEmail, String password) {
        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            view.showLoginError("Please fill in all fields");
            return;
        }
        int res = UserService.getUserServiceInstance(getApplicationContext()).authenUser(usernameOrEmail, password);
        String userDocId = UserUtils.generateUserDocId(usernameOrEmail);

        if (res == 200) {
            UserService.getUserServiceInstance(getApplicationContext()).setCurrentUser(usernameOrEmail);
            // Call to download content files before calling recommendation services
            downloadContentFile();
            view.showLoginSuccess();
        } else if (res == 300) {
            view.showLoginError("Wrong password");
        } else {
            view.showLoginError("User not found");
        }
    }

    private void downloadContentFile() {
        String movieContentFile = getApplicationContext().getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MOVIE_CONTENT_FILE_NAME;
        String musicContentFile = getApplicationContext().getExternalFilesDir(null).toString() + "/" + FileUtil.LOCAL_MUSIC_CONTENT_FILE_NAME;
        new FileUtil.DownloadFileTask().execute(FileUtil.MUSIC_CONTENT_FILE_URL, musicContentFile);
        new FileUtil.DownloadFileTask().execute(FileUtil.MOVIE_CONTENT_FILE_URL, movieContentFile);
    }
}
