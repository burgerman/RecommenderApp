package com.group7.recommenderapp.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.ui.movie.MovieActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements MusicContract.View {

    private RecyclerView musicRecyclerView;
    private ProgressBar progressBar;
    private MusicAdapter musicAdapter;
    private MusicContract.Presenter presenter;
    private ImageButton heartButton;
    private FloatingActionButton feedbackButton;
    private LinearLayout feedbackContainer;
    private EditText feedbackEditText;
    private TextView musicDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        Toolbar toolbar = findViewById(R.id.toolbar_music);
        setSupportActionBar(toolbar);
        ImageView homeIcon = findViewById(R.id.homeIcon_music);
        ImageView movieIcon = findViewById(R.id.movieicon_music);
        ImageView profileIcon = findViewById(R.id.profileicon_music);

        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        progressBar = findViewById(R.id.progressBar_music);
        heartButton = findViewById(R.id.heartButton_music);
        feedbackButton = findViewById(R.id.feedbackButton_music);
        feedbackContainer = findViewById(R.id.feedbackContainer_music);
        feedbackEditText = findViewById(R.id.feedbackEditText_music);
        musicDetailsTextView = findViewById(R.id.musicDetailsTextView);

        presenter = new MusicPresenter(this, this);

        setupRecyclerView();
        setupHeartButton();
        setupFeedbackButton();
        homeIcon.setOnClickListener(v -> presenter.onHomeIconClicked());
        movieIcon.setOnClickListener(v -> presenter.onMovieIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
        presenter.loadMusic();
    }

    private void setupRecyclerView() {
        musicAdapter = new MusicAdapter(new ArrayList<>(), music -> presenter.onMusicItemClicked(music));
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicRecyclerView.setAdapter(musicAdapter);
    }

    private void setupHeartButton() {
        heartButton.setOnClickListener(v -> presenter.onHeartButtonClicked());
    }

    private void setupFeedbackButton() {
        feedbackButton.setOnClickListener(v -> {
            feedbackContainer.setVisibility(View.VISIBLE);
            feedbackButton.setVisibility(View.GONE);
        });

        findViewById(R.id.submitFeedbackButton_music).setOnClickListener(v -> {
            String feedback = feedbackEditText.getText().toString();
            presenter.onFeedbackSubmitted(feedback);
            feedbackEditText.setText("");
            feedbackContainer.setVisibility(View.GONE);
            feedbackButton.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showMusic(List<ContentItem> music) {
        musicAdapter.setMusic(music);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateHeartButton(boolean isFilled) {
        heartButton.setImageResource(isFilled ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
    }

    @Override
    public void showFeedbackSubmitted() {
        Snackbar.make(findViewById(android.R.id.content), "Feedback submitted", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMusicDetails(MusicItem music) {
        String details = String.format("Title: %s\nGenre: %s\nAverage_rating: %f",
                music.getTitle(), music.getGenres(), music.getScore());
        musicDetailsTextView.setText(details);
        musicDetailsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToHomeTap() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToMovieTap() {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToProfile() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}
