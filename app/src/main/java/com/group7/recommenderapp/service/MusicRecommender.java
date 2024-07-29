package com.group7.recommenderapp.service;

import android.content.Context;
import android.util.Log;
import androidx.annotation.WorkerThread;
import com.group7.recommenderapp.util.FileUtilMusic;
import com.group7.recommenderapp.entities.MusicItem;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

public class MusicRecommender implements RecommenderService<MusicItem> {
    private static final String TAG = "MusicBasedRecommender";

    private Interpreter modelInterpreter;
    private final Map<Integer, MusicItem> candidates = new HashMap<>();
    private final Map<String, Integer> genres = new HashMap<>();

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

    public static synchronized RecommenderService getInstance(Context ctx, MusicConfig config) {
        if (musicRecommenderInstance == null) {
            musicRecommenderInstance = new MusicRecommender(ctx, config);
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
        try {
            ByteBuffer buffer = FileUtilMusic.loadModelFile(context.getAssets(), config.model);
            modelInterpreter = new Interpreter(buffer);
            Log.i(TAG, "Music Model loaded");
        } catch (IOException e) {
            Log.e(TAG, "Error loading music model: " + e.getMessage());
        }
    }

    @WorkerThread
    private synchronized void loadCandidateList() {
        try {
            Collection<MusicItem> collection = FileUtilMusic.loadMusicList(this.context.getAssets(), config.musicList);
            candidates.clear();
            for (MusicItem item : collection) {
                Log.d(TAG, String.format("Load candidate: %s", item));
                candidates.put(item.getId(), item);
            }
            Log.v(TAG, "Candidate list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, "Error loading candidate list: " + ex.getMessage());
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
            Log.e(TAG, "Error loading genre list: " + ex.getMessage());
        }
    }

    @Override
    public void unload() {
        if (modelInterpreter != null) {
            modelInterpreter.close();
            modelInterpreter = null;
        }
    }

    private int[] preprocessIds(List<MusicItem> selectedMusic, int length) {
        int[] inputIds = new int[length];
        Arrays.fill(inputIds, config.pad);
        int i = 0;
        for (MusicItem item : selectedMusic) {
            if (i >= inputIds.length) break;
            inputIds[i++] = item.getId();
        }
        return inputIds;
    }

    private float[] preprocessRatings(List<MusicItem> selectedMusic, int length) {
        float[] inputRatings = new float[length];
        Arrays.fill(inputRatings, config.pad);
        int i = 0;
        for (MusicItem item : selectedMusic) {
            if (i >= inputRatings.length) break;
            inputRatings[i++] = 1.0f;
        }
        return inputRatings;
    }

    private int[] preprocessGenres(List<MusicItem> selectedMusic, int length) {
        int[] inputGenres = new int[length];
        Arrays.fill(inputGenres, config.unknownGenre);
        int i = 0;
        for (MusicItem item : selectedMusic) {
            if (i >= inputGenres.length) break;
            List<String> itemGenres = item.getGenres();
            if (itemGenres != null) {
                for (String genre : itemGenres) {
                    if (i >= inputGenres.length) break;
                    inputGenres[i++] = genres.getOrDefault(genre, config.unknownGenre);
                }
            }
        }
        return inputGenres;
    }

    @WorkerThread
    private synchronized Object[] preprocess(List<MusicItem> selectedMusic) {
        List<Object> inputs = new ArrayList<>();
        List<MusicConfig.Feature> sortedFeatures = new ArrayList<>(config.inputs);
        sortedFeatures.sort(Comparator.comparingInt(feature -> feature.index));

        for (MusicConfig.Feature feature : sortedFeatures) {
            switch (feature.name) {
                case MusicConfig.FEATURE_MUSIC:
                    inputs.add(preprocessIds(selectedMusic, feature.inputLength));
                    break;
                case MusicConfig.FEATURE_GENRE:
                    inputs.add(preprocessGenres(selectedMusic, feature.inputLength));
                    break;
                case MusicConfig.FEATURE_RATING:
                    inputs.add(preprocessRatings(selectedMusic, feature.inputLength));
                    break;
                default:
                    Log.e(TAG, String.format("Invalid feature: %s", feature.name));
            }
        }
        return inputs.toArray();
    }

    @WorkerThread
    private synchronized List<MusicItem> postprocess(int[] outputIds, float[] confidences, List<MusicItem> selectedMusic) {
        final List<MusicItem> results = new ArrayList<>();

        for (int i = 0; i < outputIds.length && results.size() < config.topK; i++) {
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
        if (genres == null || genres.isEmpty()) {
            Log.w(TAG, "No genres provided for recommendation");
            return new ArrayList<>();
        }

        return candidates.values().stream()
                .filter(p -> p.getGenres() != null && !Collections.disjoint(p.getGenres(), genres))
                .sorted(Comparator.comparing(MusicItem::getScore).reversed())
                .limit(config.topK)
                .collect(Collectors.toList());
    }

    @Override
    public List<MusicItem> recommendByItem(List<MusicItem> selectedMusic) {
        if (selectedMusic == null || selectedMusic.isEmpty()) {
            Log.w(TAG, "No music items provided for recommendation");
            return new ArrayList<>();
        }

        Object[] inputs = preprocess(selectedMusic);

        int[] outputIds = new int[config.outputLength];
        float[] confidences = new float[config.outputLength];
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(config.outputIdsIndex, outputIds);
        outputs.put(config.outputScoresIndex, confidences);

        modelInterpreter.runForMultipleInputsOutputs(inputs, outputs);

        return postprocess(outputIds, confidences, selectedMusic);
    }
}
