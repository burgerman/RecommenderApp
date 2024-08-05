package com.group7.recommenderapp.ui.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<ContentItem> music;
    private OnMusicItemClickListener listener;

    public MusicAdapter(List<ContentItem> music, OnMusicItemClickListener listener) {
        this.music = music;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        ContentItem musicItem = music.get(position);
        holder.bind(musicItem, listener);
    }

    @Override
    public int getItemCount() {
        if(music!=null) {
            return music.size();
        }
        return 0;
    }

    public void setMusic(List<ContentItem> music) {
        this.music = music;
        notifyDataSetChanged();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView genreTextView;
        CheckBox selectCheckBox;

        MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            genreTextView = itemView.findViewById(R.id.musicGenre);
            selectCheckBox = itemView.findViewById(R.id.selectCheckBox);
        }

        void bind(final ContentItem music, final OnMusicItemClickListener listener) {
            titleTextView.setText(music.getTitle());
            genreTextView.setText(music.getGenres().toString());

            itemView.setOnClickListener(v -> listener.onMusicItemClick(music));

            selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                music.setSelected(isChecked);
            });
        }
    }

    public interface OnMusicItemClickListener {
        void onMusicItemClick(ContentItem music);
    }
}
