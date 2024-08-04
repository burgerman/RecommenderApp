package com.group7.recommenderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.ui.music.MusicAdapter;
import com.group7.recommenderapp.ui.music.MusicContract;
import com.group7.recommenderapp.ui.music.MusicPresenter;
import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment implements MusicContract.View {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MusicContract.Presenter presenter;
    private MusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_recommendations, container, false);
        recyclerView = view.findViewById(R.id.recommendedMusicRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new MusicPresenter(this, requireContext());
        setupRecyclerView();
        presenter.loadMusic();
    }

    private void setupRecyclerView() {
        musicAdapter = new MusicAdapter(new ArrayList<>(), music -> presenter.onMusicItemClicked(music));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(musicAdapter);
    }

    @Override
    public void showMusic(List<MusicItem> music) {
        musicAdapter.setMusic(music);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateHeartButton(boolean isFilled) {

    }

    @Override
    public void showFeedbackSubmitted() {

    }

    @Override
    public void showMusicDetails(MusicItem music) {

    }
}
