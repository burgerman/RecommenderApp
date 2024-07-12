package com.group7.recommenderapp.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.group7.recommenderapp.util.FileUtil;
import com.group7.recommenderapp.entities.MovieItem;

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


public class MovieRecommender implements RecommenderService<MovieItem> {
    private static final String TAG = "MovieBasedRecommender";

    private Interpreter modelInterpreter;
    final Map<Integer, MovieItem> candidates = new HashMap<>();
    final Map<String, Integer> genres = new HashMap<>();

    private final Context context;
    private final MovieConfig config;

    private static RecommenderService movieRecommenderInstance = null;

    private MovieRecommender(Context context, MovieConfig config) {
        this.context = context;
        this.config = config;
        if (!config.validate()) {
            Log.e(TAG, "Config is not valid.");
        }
    }

    public static RecommenderService getInstance(Context ctx, MovieConfig config) {
        synchronized (MusicRecommender.class) {
            if(movieRecommenderInstance == null) {
                movieRecommenderInstance = new MovieRecommender(ctx, config);
            }
        }
        return movieRecommenderInstance;
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
            ByteBuffer buffer = FileUtil.loadModelFile(context.getAssets(), config.model);
            modelInterpreter = new Interpreter(buffer);
            Log.i(TAG,"Movie Model loaded");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
    /** Load recommendation candidate list. */
    @WorkerThread
    private synchronized void loadCandidateList() {
        try {
            Collection<MovieItem> collection =
                    FileUtil.loadMovieList(this.context.getAssets(), config.movieList);
            candidates.clear();
            for (MovieItem item : collection) {
                Log.d(TAG, String.format("Load candidate: %s", item));
                candidates.put(item.id, item);
            }
            Log.v(TAG, "Candidate list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /** Load movie genre list. */
    @WorkerThread
    private synchronized void loadGenreList() {
        try {
            List<String> genreList = FileUtil.loadGenreList(this.context.getAssets(), config.genreList);
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

    int[] preprocessIds(List<MovieItem> selectedMovies, int length) {
        int[] inputIds = new int[length];
        Arrays.fill(inputIds, config.pad); // Fill inputIds with the default.
        int i = 0;
        for (MovieItem item : selectedMovies) {
            if (i >= inputIds.length) {
                break;
            }
            inputIds[i] = item.id;
            ++i;
        }
        return inputIds;
    }

    int[] preprocessGenres(List<MovieItem> selectedMovies, int length) {
        // Fill inputGenres.
        int[] inputGenres = new int[length];
        Arrays.fill(inputGenres, config.unknownGenre); // Fill inputGenres with the default.
        int i = 0;
        for (MovieItem item : selectedMovies) {
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

    /** Given a list of selected items, preprocess to get tflite input. */
    @WorkerThread
    synchronized Object[] preprocess(List<MovieItem> selectedMovies) {
        List<Object> inputs = new ArrayList<>();

        // Sort features.
        List<MovieConfig.Feature> sortedFeatures = new ArrayList<>(config.inputs);
        Collections.sort(sortedFeatures, (MovieConfig.Feature a, MovieConfig.Feature b) -> Integer.compare(a.index, b.index));

        for (MovieConfig.Feature feature : sortedFeatures) {
            if (MovieConfig.FEATURE_MOVIE.equals(feature.name)) {
                inputs.add(preprocessIds(selectedMovies, feature.inputLength));
            } else if (MovieConfig.FEATURE_GENRE.equals(feature.name)) {
                inputs.add(preprocessGenres(selectedMovies, feature.inputLength));
            } else {
                Log.e(TAG, String.format("Invalid feature: %s", feature.name));
            }
        }
        return inputs.toArray();
    }

    /** Postprocess to gets results from tflite inference. */
    @WorkerThread
    synchronized List<MovieItem> postprocess(
            int[] outputIds, float[] confidences, List<MovieItem> selectedMovies) {
        final ArrayList<MovieItem> results = new ArrayList<>();

        // Add recommendation results. Filter null or contained items.
        for (int i = 0; i < outputIds.length; i++) {
            if (results.size() >= config.topK) {
                Log.v(TAG, String.format("Selected top K: %d. Ignore the rest.", config.topK));
                break;
            }

            int id = outputIds[i];
            MovieItem item = candidates.get(id);
            if (item == null) {
                Log.v(TAG, String.format("Inference output[%d]. Id: %s is null", i, id));
                continue;
            }
            if (selectedMovies.contains(item)) {
                Log.v(TAG, String.format("Inference output[%d]. Id: %s is contained", i, id));
                continue;
            }
            item.setConfidence(confidences[i]);
//            MovieItem result = new ResultItem(id, item, confidences[i]);
            results.add(item);
            Log.v(TAG, String.format("Inference output[%d]. Result: %s", i, item));
        }

        return results;
    }

    @Override
    public List<MovieItem> recommendByGenre(List<String> genres) {
        // read movie items from the list
        Collection<MovieItem> items = candidates.values();

        // filter on the items with overlapping genres and score(public voting)
        List<MovieItem> result = items.stream()
                .filter(p->p.getGenres().stream().anyMatch(genres::contains))
                .sorted(Comparator.comparing(MovieItem::getScore).reversed())
                .collect(Collectors.toList());
        // return the topk list of filtered and
        return result.subList(0, config.topK);
    }

    @Override
    public List<MovieItem> recommendByItem(List<MovieItem> selectedMovies) {
        Object[] inputs = preprocess(selectedMovies);

        // Run inference.
        int[] outputIds = new int[config.outputLength];
        float[] confidences = new float[config.outputLength];
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(config.outputIdsIndex, outputIds);
        outputs.put(config.outputScoresIndex, confidences);
        modelInterpreter.runForMultipleInputsOutputs(inputs, outputs);

        return postprocess(outputIds, confidences, selectedMovies);
    }
}
