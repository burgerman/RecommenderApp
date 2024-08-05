/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.group7.recommenderapp.ui.music;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of recommended music items.
 *
 * <p>Activities containing this fragment MUST implement the {@link
 * OnListFragmentInteractionListener} interface.
 */
public class RecommendationFragment extends Fragment {
  private static final String ARG_COLUMN_COUNT = "recommendation-fragment-column-count";
  private int columnCount = 1;
  private OnListFragmentInteractionListener listener;

  private RecyclerView recyclerView;
  private List<ContentItem> recommendedMusic = new ArrayList<>();

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public RecommendationFragment() {}

  // TODO: Customize parameter initialization
  @SuppressWarnings("unused")
  public static RecommendationFragment newInstance(int columnCount) {
    RecommendationFragment fragment = new RecommendationFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COLUMN_COUNT, columnCount);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      columnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.mu_re_fragment_recommendation_list, container, false);

    // Set the adapter
    if (view instanceof RecyclerView) {
      Context context = view.getContext();
      recyclerView = (RecyclerView) view;
      if (columnCount <= 1) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
      } else {
        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
      }
      setRecommendations(recommendedMusic);
    }
    return view;
  }

  public void setRecommendations(List<ContentItem> recommendedMusic) {
    this.recommendedMusic = recommendedMusic;
    recyclerView.setAdapter(
        new RecommendationRecyclerViewAdapter(this.recommendedMusic, listener));
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnListFragmentInteractionListener) {
      listener = (OnListFragmentInteractionListener) context;
    } else {
      throw new IllegalStateException(
          context + " must implement OnListFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }

  /**
   * This interface must be implemented by activities that contain this fragment to allow an
   * interaction in this fragment to be communicated to the activity and potentially other fragments
   * contained in that activity.
   *
   * <p>See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with
   * Other Fragments</a> for more information.
   */
  public interface OnListFragmentInteractionListener {
    void onClickRecommendedMusic(MusicItem item);
  }
}
