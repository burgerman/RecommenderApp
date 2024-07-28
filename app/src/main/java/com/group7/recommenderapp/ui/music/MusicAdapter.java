package com.group7.recommenderapp.ui.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.MusicItem;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<MusicItem> musicList;
    private OnMusicClickListener listener;

    public interface OnMusicClickListener {
        void onMusicClick(MusicItem music);
    }

    public MusicAdapter(List<MusicItem> musicList, OnMusicClickListener listener) {
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicItem music = musicList.get(position);
        holder.titleTextView.setText(music.getTitle());
        holder.artistTextView.setText(music.getArtist());
        holder.itemView.setOnClickListener(v -> listener.onMusicClick(music));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void setMusic(List<MusicItem> musicList) {
        this.musicList = musicList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            artistTextView = itemView.findViewById(R.id.musicArtist);
        }
    }
}
