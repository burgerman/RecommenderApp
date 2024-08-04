package com.group7.recommenderapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.ui.movie.MovieAdapter;
import com.group7.recommenderapp.ui.movie.MovieContract;
import com.group7.recommenderapp.ui.movie.MoviePresenter;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment implements MovieContract.View {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MovieContract.Presenter presenter;
    private MovieAdapter movieAdapter;

    public static final String MOVIE_RECOMMEND_FRAG = "movie_recommend_general";

    public static MovieFragment newInstance(List<ContentItem> recommendations) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MOVIE_RECOMMEND_FRAG, (ArrayList<? extends Parcelable>) new ArrayList<>(recommendations));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_recommendations, container, false);
        recyclerView = view.findViewById(R.id.recommendedMoviesRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MoviePresenter(this, requireContext());
        setupRecyclerView();
        presenter.loadMovies();
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> presenter.onMovieItemClicked(movie));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(movieAdapter);
    }

    public void showMovies(List<ContentItem> movies) {
        movieAdapter.setMovies(movies);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void updateHeartButton(boolean isFilled) {
        // This method is handled in MovieActivity
    }

    @Override
    public void showFeedbackSubmitted() {

    }

    @Override
    public void showMovieDetails(MovieItem movie) {

    }

    @Override
    public void showMessage(String message) {
        // This method is handled in MovieActivity
    }

    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
