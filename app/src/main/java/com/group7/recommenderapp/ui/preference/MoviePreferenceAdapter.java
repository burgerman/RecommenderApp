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

public class MoviePreferenceAdapter extends RecyclerView.Adapter<MoviePreferenceAdapter.ViewHolder> {

    private final List<String> moviePreferences;
    private final List<String> selectedPreferences;

    public MoviePreferenceAdapter(List<String> moviePreferences, List<String> selectedPreferences) {
        this.moviePreferences = moviePreferences;
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
        String preference = moviePreferences.get(position);
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

    public void setSelectedPreferences(List<String> selectedPreferences) {
        this.selectedPreferences.clear();
        this.selectedPreferences.addAll(selectedPreferences);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviePreferences.size();
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
