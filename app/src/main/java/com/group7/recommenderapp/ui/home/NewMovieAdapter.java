package com.group7.recommenderapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class NewMovieAdapter extends RecyclerView.Adapter<NewMovieAdapter.MovieViewHolder> {
    private List<ContentItem> movies = new ArrayList<>();

    @NonNull
    @Override
    public NewMovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_recommendation, parent, false);
        return new NewMovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMovieAdapter.MovieViewHolder holder, int position) {
        // Bind data to the view holder
        ContentItem musicItem = movies.get(position);
        // Set data to views
        holder.bind(musicItem);
    }

    @Override
    public int getItemCount() {
        if(movies!=null) {
            return movies.size();
        }
        return 0;
    }

    public void setMovies(List<ContentItem> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView movieTitle;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.movieTitle2);
        }

        public void bind(ContentItem movie) {
            movieTitle.setText(movie.getTitle());
        }
    }
}
