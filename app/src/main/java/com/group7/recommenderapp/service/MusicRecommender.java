package com.group7.recommenderapp.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.group7.recommenderapp.util.FileUtilMusic;
import com.group7.recommenderapp.entities.MusicItem;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MusicRecommender implements RecommenderService<MusicItem> {
    private static final String TAG = "MusicBasedRecommender";

    private Interpreter modelInterpreter;
    final Map<Integer, MusicItem> candidates = new HashMap<>();
    final Map<String, Integer> genres = new HashMap<>();

    private final Context context;
    private final MusicConfig config;

    private static RecommenderService musicRecommenderInstance = null;

    private MusicRecommender(Context context, MusicConfig config) {
        this.context = context;
        this.config = config;
        if (!config.validate()) {
            Log.e(TAG, "Config is not valid.");
        }
    }

    public static RecommenderService getInstance(Context ctx, MusicConfig config) {
        synchronized (MusicRecommender.class) {
            if(musicRecommenderInstance == null) {
                musicRecommenderInstance = new MusicRecommender(ctx, config);
            }
        }
        return musicRecommenderInstance;
    }

    @Override
    public void load() {
        loadModel();
        loadCandidateList();
        if (config.useGenres()) {
            loadGenreList();
        }
    }

    private void loadModel() {
        try{
            ByteBuffer buffer = FileUtilMusic.loadModelFile(context.getAssets(), config.model);
            modelInterpreter = new Interpreter(buffer);
            Log.i(TAG,"Music Model loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @WorkerThread
    private synchronized void loadCandidateList() {
        try {
            Collection<MusicItem> collection =
                    FileUtilMusic.loadMusicList(this.context.getAssets(), config.musicList);
            candidates.clear();
            for (MusicItem item : collection) {
                Log.d(TAG, String.format("Load candidate: %s", item));
                candidates.put(item.id, item);
            }
            Log.v(TAG, "Candidate list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @WorkerThread
    private synchronized void loadGenreList() {
        try {
            List<String> genreList = FileUtilMusic.loadGenreList(this.context.getAssets(), config.genreList);
            genres.clear();
            for (String genre : genreList) {
                Log.d(TAG, String.format("Load genre: \"%s\"", genre));
                genres.put(genre, genres.size());
            }
            Log.v(TAG, "Genre list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void unload() {
        if(modelInterpreter!=null) modelInterpreter.close();
    }

    int[] preprocessIds(List<MusicItem> selectedMovies, int length) {
        int[] inputIds = new int[length];
        Arrays.fill(inputIds, config.pad); // Fill inputIds with the default.
        int i = 0;
        for (MusicItem item : selectedMovies) {
            if (i >= inputIds.length) {
                break;
            }
            inputIds[i] = item.id;
            ++i;
        }
        return inputIds;
    }

    float[] preprocessRatings(List<MusicItem> selectedMovies, int length) {
        float[] inputRatings = new float[length];
        Arrays.fill(inputRatings, config.pad); // Fill inputIds with the default.
        int i = 0;
        for (MusicItem item : selectedMovies) {
            if (i >= inputRatings.length) {
                break;
            }
            // all selected music will be assigned rating to 1.0, it can be extended with more specific rating on UI if needed.
            inputRatings[i] = 1.0f;
            ++i;
        }
        return inputRatings;
    }

    int[] preprocessGenres(List<MusicItem> selectedMovies, int length) {
        // Fill inputGenres.
        int[] inputGenres = new int[length];
        Arrays.fill(inputGenres, config.unknownGenre); // Fill inputGenres with the default.
        int i = 0;
        for (MusicItem item : selectedMovies) {
            if (i >= inputGenres.length) {
                break;
            }
            for (String genre : item.getGenres()) {
                if (i >= inputGenres.length) {
                    break;
                }
                inputGenres[i] = genres.containsKey(genre) ? genres.get(genre) : config.unknownGenre;
                ++i;
            }
        }
        return inputGenres;
    }

    @WorkerThread
    synchronized Object[] preprocess(List<MusicItem> selectedMusic) {
        List<Object> inputs = new ArrayList<>();

        List<MusicConfig.Feature> sortedFeatures = new ArrayList<>(config.inputs);
        Collections.sort(sortedFeatures, (MusicConfig.Feature a, MusicConfig.Feature b) -> Integer.compare(a.index, b.index));

        for (MusicConfig.Feature feature : sortedFeatures) {
            if (MusicConfig.FEATURE_MUSIC.equals(feature.name)) {
                inputs.add(preprocessIds(selectedMusic, feature.inputLength));
            } else if (MusicConfig.FEATURE_GENRE.equals(feature.name)) {
                inputs.add(preprocessGenres(selectedMusic, feature.inputLength));
            } else if(MusicConfig.FEATURE_RATING.equals(feature.name)) {
                inputs.add(preprocessRatings(selectedMusic, feature.inputLength));
            } else {
                Log.e(TAG, String.format("Invalid feature: %s", feature.name));
            }
        }
        return inputs.toArray();
    }

    @WorkerThread
    synchronized List<MusicItem> postprocess(
            int[] outputIds, float[] confidences, List<MusicItem> selectedMusic) {
        final ArrayList<MusicItem> results = new ArrayList<>();

        for (int i = 0; i < outputIds.length; i++) {
            if (results.size() >= config.topK) {
                Log.v(TAG, String.format("Selected top K: %d. Ignore the rest.", config.topK));
                break;
            }

            int id = outputIds[i];
            MusicItem item = candidates.get(id);
            if (item == null) {
                Log.v(TAG, String.format("Inference output[%d]. Id: %s is null", i, id));
                continue;
            }
            if (selectedMusic.contains(item)) {
                Log.v(TAG, String.format("Inference output[%d]. Id: %s is contained", i, id));
                continue;
            }
            item.setConfidence(confidences[i]);
            results.add(item);
            Log.v(TAG, String.format("Inference output[%d]. Result: %s", i, item));
        }

        return results;
    }

    @Override
    public List<MusicItem> recommendByGenre(List<String> genres) {
        Collection<MusicItem> items = candidates.values();

        List<MusicItem> result = items.stream()
                .filter(p->p.getGenres().stream().anyMatch(genres::contains))
                .sorted(Comparator.comparing(MusicItem::getScore).reversed())
                .collect(Collectors.toList());
        return result.subList(0, config.topK);
    }

    @Override
    public List<MusicItem> recommendByItem(List<MusicItem> selectedMusic) {
        Object[] inputs = preprocess(selectedMusic);

        int[] outputIds = new int[config.outputLength];
        float[] confidences = new float[config.outputLength];
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(config.outputIdsIndex, outputIds);
        outputs.put(config.outputScoresIndex, confidences);
        modelInterpreter.runForMultipleInputsOutputs(inputs, outputs);

        return postprocess(outputIds, confidences, selectedMusic);
    }

    // Additional helper methods (preprocessIds, preprocessRatings, preprocessGenres) would be similar to MovieRecommender
    // ...
}