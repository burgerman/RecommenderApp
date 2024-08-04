package com.group7.recommenderapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.fragments.MovieFragment;
import com.group7.recommenderapp.fragments.MusicFragment;
import com.group7.recommenderapp.service.UserService;
import com.group7.recommenderapp.ui.login.LoginActivity;
import com.group7.recommenderapp.ui.preference.PreferenceSelectionActivity;
import com.group7.recommenderapp.util.UserUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity implements UserProfileContract.View {

    private ImageView profileImage;
    private EditText nameEditText, ageEditText, genderEditText;
    private TextView preferencesTextView;
    private Button saveButton, logoutButton, moviePreferencesButton, musicPreferencesButton;
    private RecyclerView genreRecyclerView, likedItemsRecyclerView;
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
        preferencesTextView = findViewById(R.id.preferencesTextView);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);
        moviePreferencesButton = findViewById(R.id.moviePreferencesButton);
        musicPreferencesButton = findViewById(R.id.musicPreferencesButton);
        genreRecyclerView = findViewById(R.id.genreRecyclerView);
        likedItemsRecyclerView = findViewById(R.id.likedItemsRecyclerView);
        presenter = new UserProfilePresenter(this, this);
        profileImage.setImageResource(R.mipmap.profile_placeholder);

        saveButton.setOnClickListener(v -> saveProfile());
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
                });
    preferencesTextView.setOnClickListener(v -> openPreferenceSelection());
        moviePreferencesButton.setOnClickListener(v -> openMoviePreferences());
        musicPreferencesButton.setOnClickListener(v -> openMusicPreferences());
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

    private void openMoviePreferences() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MovieFragment())
                .addToBackStack(null)
                .commit();
    }

    private void openMusicPreferences() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new MusicFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void displayUserProfile(UserProfile userProfile) {
        String userName = UserService.getUserServiceInstance(this).getCurrentUser();
        nameEditText.setText(userName);

        ageEditText.setText(String.valueOf(userProfile.getAge()));
        genderEditText.setText(userProfile.getGender());

        GenreAdapter movieGenreAdapter = new GenreAdapter((List<String>)userProfile.getPreferences().getPreferenceDict().get("categoriesClass1"));
        genreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        genreRecyclerView.setAdapter(movieGenreAdapter);
        if(userProfile.getPreferences().getPreferenceDict().get("categoriesClass2")!=null) {
            GenreAdapter musicGenreAdapter = new GenreAdapter((List<String>)userProfile.getPreferences().getPreferenceDict().get("categoriesClass2"));
            genreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            genreRecyclerView.setAdapter(musicGenreAdapter);
        }

        LikedItemsAdapter likedMovieItemsAdapter = new LikedItemsAdapter((List<String>)userProfile.getPreferences().getPreferenceDict().get("LikedMovieList"));
        likedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        likedItemsRecyclerView.setAdapter(likedMovieItemsAdapter);

        if(userProfile.getPreferences().getPreferenceDict().get("LikedMusicList")!=null) {
            LikedItemsAdapter likedMusicItemsAdapter = new LikedItemsAdapter((List<String>)userProfile.getPreferences().getPreferenceDict().get("LikedMusicList"));
            likedItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            likedItemsRecyclerView.setAdapter(likedMusicItemsAdapter);
        }
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
