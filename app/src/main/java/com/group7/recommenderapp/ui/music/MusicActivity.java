package com.group7.recommenderapp.ui.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.ui.movie.MovieActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements MusicContract.View,
        MusicFragment.OnListFragmentInteractionListener, RecommendationFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MusicActivity";

    private ProgressBar progressBar;
    private MusicContract.Presenter presenter;
    private FloatingActionButton feedbackButton;
    private LinearLayout feedbackContainer;
    private EditText feedbackEditText;

    private RecommenderService client;
    private final List<MusicItem> allMusic = new ArrayList<>();
    private final List<MusicItem> selectedMusic = new ArrayList<>();

    private Handler handler;
    private MusicFragment musicFragment;
    private RecommendationFragment recommendationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initializeViews();
        setupToolbar();
        setupFeedbackButton();
        initializeFragments();
        initializeRecommenderService();

        presenter = new MusicPresenter(this, this);
        handler = new Handler();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar_music);
        feedbackButton = findViewById(R.id.feedbackButton_music);
        feedbackContainer = findViewById(R.id.feedbackContainer_music);
        feedbackEditText = findViewById(R.id.feedbackEditText_music);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_music);
        setSupportActionBar(toolbar);
        ImageView homeIcon = findViewById(R.id.homeIcon_music);
        ImageView movieIcon = findViewById(R.id.movieicon_music);
        ImageView profileIcon = findViewById(R.id.profileicon_music);

        homeIcon.setOnClickListener(v -> presenter.onHomeIconClicked());
        movieIcon.setOnClickListener(v -> presenter.onMovieIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
    }

    private void initializeFragments() {
        musicFragment = new MusicFragment();
        recommendationFragment = new RecommendationFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.music_fragment_container, musicFragment);
        transaction.replace(R.id.recommendation_fragment_container, recommendationFragment);
        transaction.commit();
    }

    private void initializeRecommenderService() {
        try {
            client = MusicRecommender.getInstance(this);
        } catch (IOException e) {
            Log.e(TAG, "Error initializing MusicRecommender", e);
            showError("Failed to initialize music recommender");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
        loadInitialMusic();
        handler.post(() -> client.load());
    }

    private void loadInitialMusic() {
        String[] genres = {"Pop", "Rock"};
        List<MusicItem> favoriteMusic = client.recommendByGenre(Arrays.asList(genres));
        if (musicFragment != null) {
            musicFragment.setMusic(favoriteMusic);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(() -> client.unload());
    }

    private void recommend(final List<MusicItem> music) {
        handler.post(() -> {
            Log.d(TAG, "Run inference with TFLite model.");
            List<ContentItem> recommendations = client.recommendByItem(music);
            showResult(recommendations);
        });
    }

    private void showResult(final List<ContentItem> recommendations) {
        runOnUiThread(() -> {
            if (recommendationFragment != null) {
                recommendationFragment.setRecommendations(recommendations);
            }
        });
    }

    @Override
    public void onItemSelectionChange(MusicItem item) {
        if (item.isSelected()) {
            if (!selectedMusic.contains(item)) {
                selectedMusic.add(item);
            }
        } else {
            selectedMusic.remove(item);
        }

        if (!selectedMusic.isEmpty()) {
            logSelectedMusic();
            recommend(selectedMusic);
        } else {
            showResult(new ArrayList<>());
        }
    }

    private void logSelectedMusic() {
        StringBuilder sb = new StringBuilder("Selected music:\n");
        for (MusicItem music : selectedMusic) {
            sb.append(String.format("  %s\n", music.getTitle()));
        }
        Log.d(TAG, sb.toString());
    }

    @Override
    public void onClickRecommendedMusic(MusicItem item) {
        String message = String.format("Clicked recommended music: %s", item.getTitle());
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        if (musicFragment != null) {
            musicFragment.setMusic((List<MusicItem>) (List<?>) music);
        }
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
    public void showFeedbackSubmitted() {
        Snackbar.make(findViewById(android.R.id.content), "Feedback submitted", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMusicDetails(MusicItem music) {
        String details = String.format("Title: %s\nGenre: %s\nRating: %.1f",
                music.getTitle(), String.join(", ", music.getGenres()), music.getScore());
        Toast.makeText(this, details, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToHomeTap() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public void navigateToMovieTap() {
        startActivity(new Intent(this, MovieActivity.class));
    }

    @Override
    public void navigateToProfile() {
        startActivity(new Intent(this, UserProfileActivity.class));
    }
}