package com.group7.recommenderapp.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<ContentItem> contentItems;

    public ViewPagerAdapter(List<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentItem contentItem = contentItems.get(position);
        holder.bind(contentItem);
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView genreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            genreTextView = itemView.findViewById(R.id.movieGenre);
        }

        public void bind(ContentItem contentItem) {
            titleTextView.setText(contentItem.getTitle());
            genreTextView.setText(contentItem.getGenres().toString());
        }
    }

}
