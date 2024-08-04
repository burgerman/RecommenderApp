package com.group7.recommenderapp.ui.movie;

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
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.ui.music.MusicActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private RecyclerView movieRecyclerView;
    private ProgressBar progressBar;
    private MovieAdapter movieAdapter;
    private MovieContract.Presenter presenter;
    private ImageButton heartButton;
    private FloatingActionButton feedbackButton;
    private LinearLayout feedbackContainer;
    private EditText feedbackEditText;
    private TextView movieDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar_movie);
        setSupportActionBar(toolbar);
        ImageView homeIcon = findViewById(R.id.homeIcon_movie);
        ImageView musicIcon = findViewById(R.id.musicicon_movie);
        ImageView profileIcon = findViewById(R.id.profileicon_movie);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        progressBar = findViewById(R.id.progressBar_movie);
        heartButton = findViewById(R.id.heartButton_movie);
        feedbackButton = findViewById(R.id.feedbackButton_movie);
        feedbackContainer = findViewById(R.id.feedbackContainer_movie);
        feedbackEditText = findViewById(R.id.feedbackEditText_movie);
        movieDetailsTextView = findViewById(R.id.movieDetailsTextView);

        presenter = new MoviePresenter(this, this);

        setupRecyclerView();
        setupHeartButton();
        setupFeedbackButton();
        homeIcon.setOnClickListener(v -> presenter.onHomeIconClicked());
        musicIcon.setOnClickListener(v -> presenter.onMusicIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
        presenter.loadMovies();
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> presenter.onMovieItemClicked(movie));
        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieRecyclerView.setAdapter(movieAdapter);
    }

    private void setupHeartButton() {
        heartButton.setOnClickListener(v -> presenter.onHeartButtonClicked());
    }

    private void setupFeedbackButton() {
        feedbackButton.setOnClickListener(v -> {
            feedbackContainer.setVisibility(View.VISIBLE);
            feedbackButton.setVisibility(View.GONE);
        });

        findViewById(R.id.submitFeedbackButton_movie).setOnClickListener(v -> {
            String feedback = feedbackEditText.getText().toString();
            presenter.onFeedbackSubmitted(feedback);
            feedbackEditText.setText("");
            feedbackContainer.setVisibility(View.GONE);
            feedbackButton.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showMovies(List<ContentItem> movies) {
        movieAdapter.setMovies(movies);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {

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
    public void showMovieDetails(MovieItem movie) {
        String details = String.format("Title: %s\nGenres: %s\nAverage_rating: %f",
                movie.getTitle(), movie.getId(), String.join(", ", movie.getGenres()), movie.getScore());
        movieDetailsTextView.setText(details);
        movieDetailsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToHomeTap() {
        Intent intent = new Intent(this, HomeActivity.class);
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
