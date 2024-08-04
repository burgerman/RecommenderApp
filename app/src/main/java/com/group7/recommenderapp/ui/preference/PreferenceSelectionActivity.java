package com.group7.recommenderapp.ui.preference;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.util.DatabaseManager;
import java.util.ArrayList;
import java.util.List;

public class PreferenceSelectionActivity extends AppCompatActivity implements PreferenceSelectionContract.View {

    private RecyclerView movieRecyclerView;
    private RecyclerView musicRecyclerView;
    private EditText movieGenreInput;
    private EditText musicGenreInput;
    private Button savePreferencesButton;
    private ProgressBar progressBar;
    private PreferenceSelectionContract.Presenter presenter;

    private MoviePreferenceAdapter movieAdapter;
    private MusicPreferenceAdapter musicAdapter;
    private String userId;
    private MovieRecommender movieRecommender;
    private MusicRecommender musicRecommender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_selection);

        userId = DatabaseManager.getSharedInstance(this).getCurrentUserDocId();
        if (userId == null) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        movieGenreInput = findViewById(R.id.movieGenreInput);
        musicGenreInput = findViewById(R.id.musicGenreInput);
        savePreferencesButton = findViewById(R.id.savePreferencesButton);
        progressBar = findViewById(R.id.progressBar);

        presenter = new PreferenceSelectionPresenter(this, this);

        setupRecyclerViews();

        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedMoviePreferences = movieAdapter.getSelectedPreferences();
                List<String> selectedMusicPreferences = musicAdapter.getSelectedPreferences();
                String additionalMoviePreference = movieGenreInput.getText().toString();
                String additionalMusicPreference = musicGenreInput.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("additionalMoviePreferences", new ArrayList<>(List.of(additionalMoviePreference.split(","))));
                bundle.putStringArrayList("additionalMusicPreferences", new ArrayList<>(List.of(additionalMusicPreference.split(","))));

                presenter.savePreferences(userId, selectedMoviePreferences, selectedMusicPreferences, bundle);
            }
        });

        presenter.loadExistingPreferences(userId);
    }

    private void setupRecyclerViews() {
        List<String> movieGenres = presenter.getMovieGenres();
        List<String> musicGenres = presenter.getMusicGenres();

        movieAdapter = new MoviePreferenceAdapter(movieGenres, new ArrayList<>());
        musicAdapter = new MusicPreferenceAdapter(musicGenres, new ArrayList<>());

        movieRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieRecyclerView.setAdapter(movieAdapter);

        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicRecyclerView.setAdapter(musicAdapter);
    }

    @Override
    public void showPreferencesSaved() {
        Toast.makeText(this, "Preferences saved successfully", Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
    public void displayExistingPreferences(List<String> moviePreferences, List<String> musicPreferences, String additionalMoviePreference, String additionalMusicPreference) {
        if (moviePreferences != null) {
            movieAdapter.setSelectedPreferences(moviePreferences);
        }
        if (musicPreferences != null) {
            musicAdapter.setSelectedPreferences(musicPreferences);
        }
        if (additionalMoviePreference != null) {
            movieGenreInput.setText(additionalMoviePreference);
        }
        if (additionalMusicPreference != null) {
            musicGenreInput.setText(additionalMusicPreference);
        }
    }
}
