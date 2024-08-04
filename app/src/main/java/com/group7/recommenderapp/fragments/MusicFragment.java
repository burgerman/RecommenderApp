package com.group7.recommenderapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
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

    public static final String MUSIC_RECOMMEND_FRAG = "movie_recommend_general";

    public static MusicFragment newInstance(List<ContentItem> recommendations) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MUSIC_RECOMMEND_FRAG, new ArrayList<>(recommendations));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_recommendations, container, false);
        recyclerView = view.findViewById(R.id.recommendedMusicRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        if(getArguments() != null) {
            List<ContentItem> recommends = getArguments().getParcelableArrayList(MUSIC_RECOMMEND_FRAG);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            musicAdapter = new MusicAdapter(recommends, music -> {presenter.onMusicItemClicked(music);});
            recyclerView.setAdapter(musicAdapter);
        }
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
    public void showMusic(List<ContentItem> music) {
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

    @Override
    public void navigateToHomeTap() {

    }

    @Override
    public void navigateToMovieTap() {

    }

    @Override
    public void navigateToProfile() {

    }
}
