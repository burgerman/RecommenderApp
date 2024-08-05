package com.group7.recommenderapp.ui.movie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.ui.music.MusicActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MovieActivity extends AppCompatActivity implements MovieContract.View,
        MovieFragment.OnListFragmentInteractionListener,RecommendationFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MovieActivity";

//    private RecyclerView movieRecyclerView;
    private ProgressBar progressBar;
//    private MovieAdapter movieAdapter;
    private MovieContract.Presenter presenter;
//    private ImageButton heartButton;
    private FloatingActionButton feedbackButton;
    private LinearLayout feedbackContainer;
    private EditText feedbackEditText;
//    private TextView movieDetailsTextView;

    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.

//    private Config config;
    private RecommenderService client;
    private final List<MovieItem> allMovies = new ArrayList<>();
    private final List<MovieItem> selectedMovies = new ArrayList<>();

    private Handler handler;
    private MovieFragment movieFragment;
    private RecommendationFragment recommendationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar toolbar = findViewById(R.id.toolbar_movie);
        setSupportActionBar(toolbar);
        ImageView homeIcon = findViewById(R.id.homeIcon_movie);
        ImageView musicIcon = findViewById(R.id.musicicon_movie);
        ImageView profileIcon = findViewById(R.id.profileicon_movie);

//        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        progressBar = findViewById(R.id.progressBar_movie);
//        heartButton = findViewById(R.id.heartButton_movie);
        feedbackButton = findViewById(R.id.feedbackButton_movie);
        feedbackContainer = findViewById(R.id.feedbackContainer_movie);
        feedbackEditText = findViewById(R.id.feedbackEditText_movie);
//        movieDetailsTextView = findViewById(R.id.movieDetailsTextView);

        presenter = new MoviePresenter(this, this);

//        setupRecyclerView();
//        setupHeartButton();
        setupFeedbackButton();
        homeIcon.setOnClickListener(v -> presenter.onHomeIconClicked());
        musicIcon.setOnClickListener(v -> presenter.onMusicIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
        presenter.loadMovies();
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tfe_re_activity_main);

        // Load config file.
//        try {
//            config = FileUtil.loadConfig(getAssets(), CONFIG_PATH);
//        } catch (IOException ex) {
//            Log.e(TAG, String.format("Error occurs when loading config %s: %s.", CONFIG_PATH, ex));
//        }

        // Load movies list.
//        try {
//            allMovies.clear();
//            allMovies.addAll(FileUtil.loadMovieList(getAssets(), config.movieList));
//        } catch (IOException ex) {
//            Log.e(TAG, String.format("Error occurs when loading movies %s: %s.", config.movieList, ex));
//        }

        try {
            client = MovieRecommender.getInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        handler = new Handler();
        movieFragment =
                (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.movie_fragment);
        recommendationFragment =
                (RecommendationFragment)
                        getSupportFragmentManager().findFragmentById(R.id.recommendation_fragment);
    }

    @SuppressWarnings("AndroidJdkLibsChecker")
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");

        // Add favorite movies to the fragment.
        String[] genres = {"Comedy", "Action"};
        List<MovieItem> favoriteMovies = client.recommendByGenre(Arrays.asList(genres));
        movieFragment.setMovies(favoriteMovies);

        handler.post(
                () -> {
                    client.load();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
        handler.post(
                () -> {
                    client.unload();
                });
    }

    /** Sends selected movie list and get recommendations. */
    private void recommend(final List<MovieItem> movies) {
        handler.post(
                () -> {
                    // Run inference with TF Lite.
                    Log.d(TAG, "Run inference with TFLite model.");
                    List<ContentItem> recommendations = client.recommendByItem(movies);

                    // Show result on screen
                    showResult(recommendations);
                });
    }

    /** Shows result on the screen. */
    private void showResult(final List<ContentItem> recommendations) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread(() -> recommendationFragment.setRecommendations(recommendations));
    }

    @Override
    public void onItemSelectionChange(MovieItem item) {
        if (item.isSelected()) {
            if (!selectedMovies.contains(item)) {
                selectedMovies.add(item);
            }
        } else {
            selectedMovies.remove(item);
        }

        if (!selectedMovies.isEmpty()) {
            // Log selected movies.
            StringBuilder sb = new StringBuilder();
            sb.append("Select movies in the following order:\n");
            for (MovieItem movie : selectedMovies) {
                sb.append(String.format("  movie: %s\n", movie));
            }
            Log.d(TAG, sb.toString());

            // Recommend based on selected movies.
            recommend(selectedMovies);
        } else {
            // Clear result list.
            showResult(new ArrayList<ContentItem>());
        }
    }

    /** Handles click event of recommended movie. */
    @Override
    public void onClickRecommendedMovie(MovieItem item) {
        // Show message for the clicked movie.
        String message = String.format("Clicked recommended movie: %s.", item.getTitle());
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


//private void setupRecyclerView() {
//        movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> presenter.onMovieItemClicked(movie));
//        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        movieRecyclerView.setAdapter(movieAdapter);
//    }

//    private void setupHeartButton() {
//        heartButton.setOnClickListener(v -> presenter.onHeartButtonClicked());
//    }

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

//    @Override
//    public void showMovies(List<ContentItem> movies) {
//        movieAdapter.setMovies(movies);
//    }

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

//    @Override
//    public void updateHeartButton(boolean isFilled) {
//        heartButton.setImageResource(isFilled ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
//    }

    @Override
    public void showFeedbackSubmitted() {
        Snackbar.make(findViewById(android.R.id.content), "Feedback submitted", Snackbar.LENGTH_SHORT).show();
    }

//    @Override
//    public void showMovieDetails(MovieItem movie) {
//        String details = String.format("Title: %s\nGenres: %s\nAverage_rating: %f",
//                movie.getTitle(), movie.getId(), String.join(", ", movie.getGenres()), movie.getScore());
//        movieDetailsTextView.setText(details);
//        movieDetailsTextView.setVisibility(View.VISIBLE);
//    }

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
