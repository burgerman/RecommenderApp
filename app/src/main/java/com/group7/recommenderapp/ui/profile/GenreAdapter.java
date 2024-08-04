package com.group7.recommenderapp.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<String> genres;

    public GenreAdapter(List<String> genres) {
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genre = genres.get(position);
        holder.genreTextView.setText(genre);
    }

    @Override
    public int getItemCount() {
        if(genres!=null){
            return genres.size();
        }
        return 0;
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        TextView genreTextView;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
        }
    }
}
