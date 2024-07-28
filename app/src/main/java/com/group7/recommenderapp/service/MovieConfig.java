package com.group7.recommenderapp.service;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public final class MovieConfig {
    private static final String TAG = "Config";
    private static final String DEFAULT_MODEL_PATH = "recommendation_rnn_i10o100.tflite";
    private static final String DEFAULT_MOVIE_LIST_PATH = "sorted_movie_vocab.json";
    private static final String DEFAULT_GENRE_LIST_PATH = "movie_genre_vocab.txt";
    private static final int DEFAULT_OUTPUT_LENGTH = 100;
    private static final int DEFAULT_TOP_K = 10;
    private static final int PAD_ID = 0;
    private static final int UNKNOWN_GENRE = 0;
    private static final int DEFAULT_OUTPUT_IDS_INDEX = 0;
    private static final int DEFAULT_OUTPUT_SCORES_INDEX = 1;
    private static final int DEFAULT_FAVORITE_LIST_SIZE = 100;

    public static final String FEATURE_MOVIE = "movieFeature";
    public static final String FEATURE_GENRE = "genreFeature";

    public static class Feature {
        public String name;
        public int index;
        public int inputLength;
    }

    public String model = DEFAULT_MODEL_PATH;
    public List<Feature> inputs = new ArrayList<>();
    public int outputLength = DEFAULT_OUTPUT_LENGTH;
    public int topK = DEFAULT_TOP_K;
    public String movieList = DEFAULT_MOVIE_LIST_PATH;
    public String genreList = DEFAULT_GENRE_LIST_PATH;
    public int pad = PAD_ID;
    public int unknownGenre = UNKNOWN_GENRE;
    public int outputIdsIndex = DEFAULT_OUTPUT_IDS_INDEX;
    public int outputScoresIndex = DEFAULT_OUTPUT_SCORES_INDEX;
    public int favoriteListSize = DEFAULT_FAVORITE_LIST_SIZE;

    public MovieConfig() {}

    public boolean validate() {
        if (inputs.isEmpty()) {
            Log.e(TAG, "config inputs should not be empty");
            return false;
        }

        boolean hasGenreFeature = false;
        for (Feature feature : inputs) {
            if (FEATURE_GENRE.equals(feature.name)) {
                hasGenreFeature = true;
                break;
            }
        }
        if (useGenres() || hasGenreFeature) {
            if (!useGenres() || !hasGenreFeature) {
                String msg = "If uses genre, must set both `genreFeature` in inputs and `genreList` as vocab.";
                if (!useGenres()) {
                    msg += "`genreList` is missing.";
                }
                if (!hasGenreFeature) {
                    msg += "`genreFeature` is missing.";
                }
                Log.e(TAG, msg);
                return false;
            }
        }

        return true;
    }

    public boolean useGenres() {
        return genreList != null;
    }
}
