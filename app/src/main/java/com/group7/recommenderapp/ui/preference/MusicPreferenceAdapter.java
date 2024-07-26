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

public class MusicPreferenceAdapter extends RecyclerView.Adapter<MusicPreferenceAdapter.ViewHolder> {

    private final List<String> musicPreferences;
    private final List<String> selectedPreferences;

    public MusicPreferenceAdapter(List<String> musicPreferences, List<String> selectedPreferences) {
        this.musicPreferences = musicPreferences;
        this.selectedPreferences = selectedPreferences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_preference, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String preference = musicPreferences.get(position);
        holder.preferenceText.setText(preference);
        holder.preferenceCheckBox.setChecked(selectedPreferences.contains(preference));
        holder.preferenceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedPreferences.add(preference);
            } else {
                selectedPreferences.remove(preference);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicPreferences.size();
    }

    public List<String> getSelectedPreferences() {
        return selectedPreferences;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView preferenceText;
        CheckBox preferenceCheckBox;

        ViewHolder(View itemView) {
            super(itemView);
            preferenceText = itemView.findViewById(R.id.preferenceText);
            preferenceCheckBox = itemView.findViewById(R.id.preferenceCheckBox);
        }
    }
}
