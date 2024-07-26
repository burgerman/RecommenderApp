package com.group7.recommenderapp.ui.movie;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.MovieItem;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieContract.View {

    private MovieContract.Presenter presenter;
    private RecyclerView movieRecyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieRecyclerView = findViewById(R.id.movieRecyclerView);
        progressBar = findViewById(R.id.progressBar);

        presenter = new MoviePresenter(this, this);
        presenter.loadMovies();
    }

    @Override
    public void showMovies(List<MovieItem> movies) {
        // TODO: Set up RecyclerView adapter and display movies
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
