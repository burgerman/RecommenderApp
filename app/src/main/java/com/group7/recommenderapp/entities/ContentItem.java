package com.group7.recommenderapp.entities;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class ContentItem {

    public int id;
    private String title;
    private List<String> genres;
    private float score;
    private boolean isSelected;

    public static final String JOINER = " | ";
    public static final String DELIMITER = "[|]";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return String.format(Locale.CANADA,
                "Id: %d, title: %s, genres: %s, score: %.2f, selected: %s",
                id, title, TextUtils.join(JOINER, genres), score, isSelected);
    }
}
