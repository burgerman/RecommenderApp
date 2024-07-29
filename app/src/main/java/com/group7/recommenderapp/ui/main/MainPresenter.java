package com.group7.recommenderapp.ui.main;

import com.group7.recommenderapp.dao.UserDao;
import com.group7.recommenderapp.entities.User;

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View view;
    private final UserDao userDao;

    public MainPresenter(MainContract.View view, UserDao userDao) {
        this.view = view;
        this.userDao = userDao;
    }

    @Override
    public void start() {
        User user = userDao.getUserByUsername("testuser");
        if (user != null) {
            view.showUserInfo(user);
        } else {
            view.showError("No test user found. Create one first.");
        }
    }

    @Override
    public void createTestUser() {
        userDao.createTestUser();
        User createdUser = userDao.getUserByUsername("testuser");
        if (createdUser != null) {
            view.showSuccess("Test user created successfully");
            view.showUserInfo(createdUser);
        } else {
            view.showError("Failed to create test user");
        }
    }
}
