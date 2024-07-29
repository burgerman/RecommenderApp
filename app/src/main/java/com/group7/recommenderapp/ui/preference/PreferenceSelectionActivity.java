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
import com.group7.recommenderapp.ui.home.HomeActivity;
import com.group7.recommenderapp.util.UserUtils;
import java.util.ArrayList;
import java.util.List;

public class PreferenceSelectionActivity extends AppCompatActivity implements PreferenceSelectionContract.View {

    private RecyclerView movieRecyclerView;
    private RecyclerView musicRecyclerView;
    private EditText additionalPreferenceInput;
    private Button savePreferencesButton;
    private ProgressBar progressBar;
    private PreferenceSelectionContract.Presenter presenter;

    private MoviePreferenceAdapter movieAdapter;
    private MusicPreferenceAdapter musicAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_selection);

        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        additionalPreferenceInput = findViewById(R.id.additionalPreferenceInput);
        savePreferencesButton = findViewById(R.id.savePreferencesButton);
        progressBar = findViewById(R.id.progressBar);

        presenter = new PreferenceSelectionPresenter(this, this);

        setupRecyclerViews();

        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedMoviePreferences = movieAdapter.getSelectedPreferences();
                List<String> selectedMusicPreferences = musicAdapter.getSelectedPreferences();
                String additionalPreference = additionalPreferenceInput.getText().toString();
                presenter.savePreferences(userId, selectedMoviePreferences, selectedMusicPreferences, additionalPreference);
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
        try {
            Toast.makeText(this, "Preferences saved successfully", Toast.LENGTH_SHORT).show();
            UserUtils.saveUserMoviePreferences(this, movieAdapter.getSelectedPreferences());
            navigateToHome();
        } catch (Exception e) {
            Log.e("PreferenceSelection", "Error saving preferences", e);
            showError("An error occurred while saving preferences. Please try again.");
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USER_ID", userId);
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
    public void displayExistingPreferences(List<String> moviePreferences, List<String> musicPreferences, String additionalPreference) {
        if (moviePreferences != null) {
            movieAdapter.setSelectedPreferences(moviePreferences);
        }
        if (musicPreferences != null) {
            musicAdapter.setSelectedPreferences(musicPreferences);
        }
        if (additionalPreference != null) {
            additionalPreferenceInput.setText(additionalPreference);
        }
    }
}
