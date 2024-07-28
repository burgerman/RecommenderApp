package com.group7.recommenderapp.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/** Config for music recommendation app. */
public final class MusicConfig {
    private static final String TAG = "MusicConfig";
    private static final String DEFAULT_MODEL_PATH = "music_trained.tflite";
    private static final String DEFAULT_MUSIC_LIST_PATH = "sorted_music_vocab.json";
    private static final String DEFAULT_GENRE_LIST_PATH = "music_genre_vocab.txt";
    private static final int DEFAULT_OUTPUT_LENGTH = 100;
    private static final int DEFAULT_TOP_K = 10;
    private static final int PAD_ID = 0;
    private static final int UNKNOWN_GENRE = 0;
    private static final int DEFAULT_OUTPUT_IDS_INDEX = 0;
    private static final int DEFAULT_OUTPUT_SCORES_INDEX = 1;
    private static final int DEFAULT_FAVORITE_LIST_SIZE = 100;

    public static final String FEATURE_MUSIC = "musicFeature";
    public static final String FEATURE_GENRE = "genreFeature";
    public static final String FEATURE_RATING = "ratingFeature";

    /** Feature of the model. */
    public static class Feature {
        /** Input feature name. */
        public String name;
        /** Input feature index. */
        public int index;
        /** Input feature length. */
        public int inputLength;
    }

    /** TF Lite model path. */
    public String model = DEFAULT_MODEL_PATH;

    /** List of input features */
    public List<Feature> inputs = new ArrayList<>();
    /** Number of output length from the model. */
    public int outputLength = DEFAULT_OUTPUT_LENGTH;
    /** Number of max results to show in the UI. */
    public int topK = DEFAULT_TOP_K;
    /** Path to the music list. */
    public String musicList = DEFAULT_MUSIC_LIST_PATH;
    /** Path to the genre list. Use genre feature if it is not null. */
    public String genreList = DEFAULT_GENRE_LIST_PATH;

    /** Id for padding. */
    public int pad = PAD_ID;

    /** Music genre for unknown. */
    public int unknownGenre = UNKNOWN_GENRE;

    /** Output index for ID. */
    public int outputIdsIndex = DEFAULT_OUTPUT_IDS_INDEX;
    /** Output index for score. */
    public int outputScoresIndex = DEFAULT_OUTPUT_SCORES_INDEX;

    /** The number of favorite music tracks for users to choose from. */
    public int favoriteListSize = DEFAULT_FAVORITE_LIST_SIZE;

    public MusicConfig() {}

    public boolean validate() {
        if (inputs.isEmpty()) {
            Log.e(TAG, "config inputs should not be empty");
            return false;
        }

        boolean hasGenreFeature = false;
        for (MusicConfig.Feature feature : inputs) {
            if (FEATURE_GENRE.equals(feature.name)) {
                hasGenreFeature = true;
                break;
            }
        }
        if (useGenres() || hasGenreFeature) {
            if (!useGenres() || !hasGenreFeature) {
                String msg =
                        "If uses genre, must set both `genreFeature` in inputs and `genreList` as vocab.";
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