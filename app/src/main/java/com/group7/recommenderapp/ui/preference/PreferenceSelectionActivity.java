package com.group7.recommenderapp.ui.preference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.ui.home.HomeActivity;
import java.util.ArrayList;
import java.util.List;

public class PreferenceSelectionActivity extends AppCompatActivity implements PreferenceSelectionContract.View {

    private RecyclerView movieRecyclerView;
    private RecyclerView musicRecyclerView;
    private Button savePreferencesButton;
    private ProgressBar progressBar;
    private PreferenceSelectionContract.Presenter presenter;

    private MoviePreferenceAdapter movieAdapter;
    private MusicPreferenceAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_selection);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        musicRecyclerView = findViewById(R.id.musicRecyclerView);
        savePreferencesButton = findViewById(R.id.savePreferencesButton);
        progressBar = findViewById(R.id.progressBar);

        presenter = new PreferenceSelectionPresenter(this, this);

        setupRecyclerViews();

        savePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selectedMoviePreferences = movieAdapter.getSelectedPreferences();
                List<String> selectedMusicPreferences = musicAdapter.getSelectedPreferences();
                presenter.savePreferences(selectedMoviePreferences, selectedMusicPreferences);
            }
        });
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
}
