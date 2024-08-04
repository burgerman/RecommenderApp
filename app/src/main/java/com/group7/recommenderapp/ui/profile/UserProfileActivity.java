package com.group7.recommenderapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.ui.login.LoginActivity;
import com.group7.recommenderapp.ui.preference.PreferenceSelectionActivity;
import com.group7.recommenderapp.ui.movie.MovieActivity;
import com.group7.recommenderapp.ui.music.MusicActivity;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    private ImageView profileImage;
    private EditText nameEditText, ageEditText, genderEditText;
    private Button saveButton, logoutButton, genrePreferencesButton, movieWishlistButton, musicWishlistButton;
    private UserProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initViews();
        presenter = new UserProfilePresenter(this, this);
        presenter.loadUserProfile();
    }

    private void initViews() {
        profileImage = findViewById(R.id.profileImage);
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);
        genrePreferencesButton = findViewById(R.id.button);
        movieWishlistButton = findViewById(R.id.moviePreferencesButton);
        musicWishlistButton = findViewById(R.id.musicPreferencesButton);

        profileImage.setImageResource(R.mipmap.profile_placeholder);

        saveButton.setOnClickListener(v -> saveProfile());
        logoutButton.setOnClickListener(v -> navigateToLogin());
        genrePreferencesButton.setOnClickListener(v -> openPreferenceSelection());
        movieWishlistButton.setOnClickListener(v -> openMovieWishlist());
        musicWishlistButton.setOnClickListener(v -> openMusicWishlist());
    }

    private void saveProfile() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", nameEditText.getText().toString());
        userInfo.put("age", Integer.parseInt(ageEditText.getText().toString()));
        userInfo.put("gender", genderEditText.getText().toString());

        presenter.updateUserProfile(userInfo);
    }

    private void openPreferenceSelection() {
        Intent intent = new Intent(this, PreferenceSelectionActivity.class);
        startActivity(intent);
    }

    private void openMovieWishlist() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    private void openMusicWishlist() {
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    @Override
    public void displayUserProfile(UserProfile userProfile) {
        String userName = UserService.getUserServiceInstance(this).getCurrentUser();
        nameEditText.setText(userName);
        ageEditText.setText(String.valueOf(userProfile.getAge()));
        genderEditText.setText(userProfile.getGender());
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProfileUpdateSuccess() {
        Snackbar.make(findViewById(android.R.id.content), "Profile updated successfully", Snackbar.LENGTH_LONG).show();
    }
}