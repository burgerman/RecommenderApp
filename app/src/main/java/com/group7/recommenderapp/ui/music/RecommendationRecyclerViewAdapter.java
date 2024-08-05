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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group7.recommenderapp.R;
import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.ui.music.RecommendationFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * RecommendationRecyclerViewAdapter: a {@link RecyclerView.Adapter} that can display a recommended
 * {@link MusicItem} and makes a call to the specified {@link OnListFragmentInteractionListener}.
 */
public class RecommendationRecyclerViewAdapter
    extends RecyclerView.Adapter<RecommendationRecyclerViewAdapter.ViewHolder> {

  private final List<ContentItem> results;
  private final OnListFragmentInteractionListener listener;

  public RecommendationRecyclerViewAdapter(
          List<ContentItem> results, OnListFragmentInteractionListener listener) {
    this.results = results;
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.mu_re_fragment_recommendation, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
//    MusicItem item = results.get(position).item;
    ContentItem item = results.get(position);
    holder.result = results.get(position);
    holder.recommendationMusicTitleView.setText(item.getTitle() + " - " + item.getGenres());
    holder.scoreView.setText(String.format("[%d]", item.id));

    holder.view.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (null != listener) {
              // Notify the active callbacks interface (the activity, if the
              // fragment is attached to one) that an item has been selected.
              // listener.onListFragmentInteraction(holder.mItem);
              listener.onClickRecommendedMusic((MusicItem)item);
            }
          }
        });
  }

  @Override
  public int getItemCount() {
    return results.size();
  }

  /** ViewHolder to display one music in list view of recommendation result. */
  public static class ViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    public final TextView scoreView;
    public final TextView recommendationMusicTitleView;
    public ContentItem result;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      this.scoreView = (TextView) view.findViewById(R.id.recommendation_score);
      this.recommendationMusicTitleView =
          (TextView) view.findViewById(R.id.recommendation_music_title);
    }

    @Override
    public String toString() {
      return super.toString() + " '" + recommendationMusicTitleView.getText() + "'";
    }
  }
}
