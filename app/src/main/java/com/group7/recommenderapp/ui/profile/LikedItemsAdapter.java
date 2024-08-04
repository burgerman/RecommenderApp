package com.group7.recommenderapp.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import java.util.List;

public class LikedItemsAdapter extends RecyclerView.Adapter<LikedItemsAdapter.ViewHolder> {

    private List<String> items;

    public LikedItemsAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liked, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTextView.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        if(items!=null) {
            return items.size();
        }
        return 0;
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTextView;

        ViewHolder(View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.likedItemText);
        }
    }
}
