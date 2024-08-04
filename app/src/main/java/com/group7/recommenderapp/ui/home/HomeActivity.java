package com.group7.recommenderapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.fragments.MovieFragment;
import com.group7.recommenderapp.fragments.MusicFragment;
import com.group7.recommenderapp.ui.login.LoginPresenter;
import com.group7.recommenderapp.ui.movie.MovieActivity;
import com.group7.recommenderapp.ui.music.MusicActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomePresenter presenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView movieIcon = findViewById(R.id.movieicon);
        ImageView musicIcon = findViewById(R.id.musicicon);
        ImageView profileIcon = findViewById(R.id.profileicon);
        progressBar = findViewById(R.id.progressBar);
        presenter = new HomePresenter(this, this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieRecommendationsContainer, new MovieFragment())
                    .replace(R.id.musicRecommendationsContainer, new MusicFragment())
                    .commit();
        }
        presenter.loadRecommendedContent();
        homeIcon.setOnClickListener(v -> presenter.onHomeIconClicked());
        movieIcon.setOnClickListener(v -> presenter.onMovieIconClicked());
        musicIcon.setOnClickListener(v -> presenter.onMusicIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
    }

    @Override
    public void showRecommendedMovieContent(List<ContentItem> content) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movieRecommendationsContainer, MovieFragment.newInstance(content))
                .commit();
    }

    @Override
    public void showRecommendedMusicContent(List<ContentItem> content) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.musicRecommendationsContainer, MusicFragment.newInstance(content))
                .commit();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void navigateToMovieTap() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMusicTap() {
        Intent intent = new Intent(this, MusicActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}