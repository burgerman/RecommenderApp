package com.group7.recommenderapp.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import java.util.ArrayList;
import java.util.List;

public class LikedItemsAdapter extends RecyclerView.Adapter<LikedItemsAdapter.ViewHolder> {
    private List<String> likedItems = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.likedItemText.setText(likedItems.get(position));
    }

    @Override
    public int getItemCount() {
        return likedItems.size();
    }

    public void setLikedItems(List<String> likedItems) {
        this.likedItems = likedItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView likedItemText;

        ViewHolder(View itemView) {
            super(itemView);
            likedItemText = itemView.findViewById(R.id.likedItemText);
        }
    }
}
