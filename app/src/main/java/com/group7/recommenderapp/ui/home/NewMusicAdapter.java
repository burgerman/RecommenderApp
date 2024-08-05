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

public class NewMusicAdapter extends RecyclerView.Adapter<NewMusicAdapter.MusicViewHolder> {
    private List<ContentItem> musics = new ArrayList<>();


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_recommendation, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        if(position<musics.size()) {
            // Bind data to the view holder
            ContentItem musicItem = musics.get(position);
            // Set data to views
            holder.bind(musicItem);
        }
    }

    @Override
    public int getItemCount() {
        if(musics!=null) {
            return musics.size();
        }
        return 0;
    }

    public void setMusic(List<ContentItem> musics) {
        this.musics = musics;
        notifyDataSetChanged();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView musicTitle;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            musicTitle = itemView.findViewById(R.id.musicTitle2);
        }

        public void bind(ContentItem music) {
            musicTitle.setText(music.getTitle());
        }
    }
}
