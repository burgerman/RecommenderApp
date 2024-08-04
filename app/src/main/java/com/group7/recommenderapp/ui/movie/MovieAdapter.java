package com.group7.recommenderapp.ui.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.MovieItem;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieItem> movies;
    private OnMovieItemClickListener listener;

    public MovieAdapter(List<MovieItem> movies, OnMovieItemClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<MovieItem> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView genreTextView;
        TextView ratingTextView;
        CheckBox selectCheckBox;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            genreTextView = itemView.findViewById(R.id.movieGenre);
            ratingTextView = itemView.findViewById(R.id.movieRating);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
        }

        void bind(final MovieItem movie, final OnMovieItemClickListener listener) {
            titleTextView.setText(movie.getTitle());
            genreTextView.setText(movie.getGenre());
            ratingTextView.setText(String.format("Rating: %.1f", movie.getRating()));

            itemView.setOnClickListener(v -> listener.onMovieItemClick(movie));

            selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                movie.setSelected(isChecked);
            });
        }
    }

    public interface OnMovieItemClickListener {
        void onMovieItemClick(MovieItem movie);
    }
}
