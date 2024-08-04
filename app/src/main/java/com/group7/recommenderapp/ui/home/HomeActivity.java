package com.group7.recommenderapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.fragments.MovieFragment;
import com.group7.recommenderapp.fragments.MusicFragment;
import com.group7.recommenderapp.ui.help.HelpActivity;
import com.group7.recommenderapp.ui.movie.MovieActivity;
import com.group7.recommenderapp.ui.music.MusicActivity;
import com.group7.recommenderapp.ui.profile.UserProfileActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private HomePresenter presenter;
    private ProgressBar progressBar;

    private NewMovieAdapter movieAdapter;
    private NewMusicAdapter musicAdapter;

    private RecyclerView movieRecommendationsRecyclerView;
    private RecyclerView musicRecommendationsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView movieIcon = findViewById(R.id.movieicon);
        ImageView musicIcon = findViewById(R.id.musicicon);
        ImageView helpIcon = findViewById(R.id.helpicon);
        ImageView profileIcon = findViewById(R.id.profileicon);

        movieRecommendationsRecyclerView = findViewById(R.id.movieRecommendationsRecyclerView);
        musicRecommendationsRecyclerView = findViewById(R.id.musicRecommendationsRecyclerView);

        movieAdapter = new NewMovieAdapter();
        musicAdapter = new NewMusicAdapter();
        movieRecommendationsRecyclerView.setAdapter(movieAdapter);
        musicRecommendationsRecyclerView.setAdapter(musicAdapter);
        movieRecommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicRecommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        presenter = new HomePresenter(this, this);
        presenter.loadMovieContent();
        presenter.loadMusicContent();
        movieIcon.setOnClickListener(v -> presenter.onMovieIconClicked());
        musicIcon.setOnClickListener(v -> presenter.onMusicIconClicked());
        profileIcon.setOnClickListener(v -> presenter.onProfileIconClicked());
        helpIcon.setOnClickListener(v -> presenter.onHelpIconClicked());

    }

    @Override
    public void showRecommendedMovieContent(List<ContentItem> content) {
        movieAdapter.setMovies(content);
    }

    @Override
    public void showRecommendedMusicContent(List<ContentItem> content) {
        musicAdapter.setMusic(content);
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
    @Override
    public void navigateToHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}