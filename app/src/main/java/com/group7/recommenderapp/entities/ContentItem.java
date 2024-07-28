package com.group7.recommenderapp.entities;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Locale;

public class ContentItem {

    /**
     * Content item id
     */
    public int id;

    /**
     * Content item title
     */
    private String title;

    /**
     * Content item genres
     */
    private List<String> genres;

    /**
     * Content item score, which will be viewed as public consensus of the heat of content
     * when it is deserialized from file, some transition in property name(count=>score) will occur
     */
    @SerializedName(value = "count")
    private int score;

    /**
     * denote if content item is selected by user
     */
    private boolean isSelected;

    /**
     * Content item confidence, this is inference confidence of the model, which will change with different selection pattern of the user
     */
    private float confidence;

    public static final String JOINER = " | ";
    public static final String DELIMITER = "[|]";

    // Default constructor
    public ContentItem() {
    }

    // Parameterized constructor
    public ContentItem(int id, String title, List<String> genres, int score) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.score = score;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return String.format(Locale.CANADA,
                "Id: %d, title: %s, genres: %s, score: %d, selected: %s, confidence: %.2f",
                id, title, TextUtils.join(JOINER, genres), score, isSelected, confidence);
    }
}
