package com.group7.recommenderapp.ui.preference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group7.recommenderapp.R;

import java.util.List;

public class MusicPreferenceAdapter extends RecyclerView.Adapter<MusicPreferenceAdapter.MusicViewHolder> {

    private final List<String> allPreferences;
    private final List<String> selectedPreferences;

    public MusicPreferenceAdapter(List<String> allPreferences, List<String> selectedPreferences) {
        this.allPreferences = allPreferences;
        this.selectedPreferences = selectedPreferences;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preference, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        String preference = allPreferences.get(position);
        holder.preferenceTextView.setText(preference);
        holder.checkBox.setChecked(selectedPreferences.contains(preference));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedPreferences.contains(preference)) {
                    selectedPreferences.add(preference);
                }
            } else {
                selectedPreferences.remove(preference);
            }
        });
    }
    public void setSelectedPreferences(List<String> preferences) {
        selectedPreferences.clear();
        selectedPreferences.addAll(preferences);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(allPreferences!=null) {
            return allPreferences.size();
        }
        return 0;
    }

    public List<String> getSelectedPreferences() {
        return selectedPreferences;
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {
        TextView preferenceTextView;
        CheckBox checkBox;

        MusicViewHolder(View itemView) {
            super(itemView);
            preferenceTextView = itemView.findViewById(R.id.preferenceTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}