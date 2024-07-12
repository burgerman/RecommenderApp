package com.group7.recommenderapp;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.service.MovieRecommender;
import com.group7.recommenderapp.service.MovieConfig;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.util.FileUtil;

import org.junit.Before;
import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RecommenderServiceTest {
//    @Mock
//    private static Context mockContext;
//    @Mock
//    private static Resources mockResources;;

    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.
    private static final String TAG = "RecommenderServiceUnitTest";
    private static MovieConfig config;
    private static RecommenderService movieRecommender;
    private static RecommenderService musicRecommender;
    private static final int RANDOM_BOUNDS = 10;
    private Context ctx;
    private final Random rand = new Random();

    @Before
    public void setUp() {
        ctx = InstrumentationRegistry.getTargetContext();

        try {
            config = FileUtil.loadMovieConfig(ctx.getAssets(), CONFIG_PATH);
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error occurs when loading config %s: %s.", CONFIG_PATH, ex));
        }
        movieRecommender = MovieRecommender.getInstance(ctx, config);
        movieRecommender.load();
//        musicRecommender = MusicBasedRecommender.getInstance(ctx);
//        musicRecommender.loadModel();

    }

    @Test
    public void movieRecommendByGenreTest() throws IOException {
        // get genre list from assets file
        List<String> genres =  FileUtil.loadGenreList(ctx.getAssets(), config.genreList);

        // randomly select some genres from asset file
        Collections.shuffle(genres);
        List<String> selectedGenres = genres.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedGenres1: " + selectedGenres);

        // assert the recommendation list is non-empty and correct in terms of length, rating/score and genre
        List<ContentItem> recommendedMovies1 = movieRecommender.recommendByGenre(selectedGenres);
        Log.i(TAG, "recommendedMovies1: " + recommendedMovies1);


        assertTrue("The recommendation list is not empty", recommendedMovies1.size() > 0);
        float lastScore = 10000;
        float newScore = 0;
        for (ContentItem item : recommendedMovies1) {
//            assertTrue("genres of recommendedMovies is contained in user's selection", genres.contains(item.getGenres()));
            newScore = item.getScore();

            assertTrue("score of recommendedMovies is in decreasing order", lastScore >= newScore);
            lastScore = newScore;
        }

        // randomly select another few genres
        Collections.shuffle(genres);
        selectedGenres = genres.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedGenres2: " + selectedGenres);

        // assert the recommendation list is different from previous one
        List<ContentItem> recommendedMovies2 = movieRecommender.recommendByGenre(selectedGenres);
        Log.i(TAG, "recommendedMovies2: " + recommendedMovies1);

        List<ContentItem> differences12 = new ArrayList<>(recommendedMovies1);
        differences12.removeAll(recommendedMovies2);

        List<ContentItem> differences21 = new ArrayList<>(recommendedMovies2);
        differences21.removeAll(recommendedMovies1);

        assertTrue("there's at least one difference between two recommendation lists", !differences12.isEmpty() || !differences21.isEmpty());
    }

    @Test
    public void movieRecommendByItemTest() throws IOException {
        // get movie list from assets file
        List<MovieItem> movies = (List<MovieItem>) FileUtil.loadMovieList(ctx.getAssets(), config.movieList);

        // randomly select some genres from asset file
        Collections.shuffle(movies);
        List<MovieItem> selectedMovies = movies.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedMovies1: " + selectedMovies);

        // assert the recommendation list is non-empty and correct in terms of length, rating/score and genre
        List<MovieItem> recommendedMovies1 = movieRecommender.recommendByItem(selectedMovies);
        Log.i(TAG, "recommendedMovies1: " + recommendedMovies1);

        assertTrue("The recommendation list is not empty", !recommendedMovies1.isEmpty());
        float lastConfidence = 1;
        float newConfidence = 0;
        for (MovieItem item : recommendedMovies1) {
            newConfidence = item.getConfidence();

            assertTrue("confidence of recommendedMovies is in decreasing order", lastConfidence >= newConfidence);
            lastConfidence = newConfidence;
        }

        // randomly select another few items
        Collections.shuffle(movies);
        selectedMovies = movies.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedMovies2: " + selectedMovies);

        // assert the recommendation list is different from previous one
        List recommendedMovies2 = movieRecommender.recommendByItem(selectedMovies);
        Log.i(TAG, "recommendedMovies2: " + recommendedMovies1);

        List<MovieItem> differences12 = new ArrayList<>(recommendedMovies1);
        differences12.removeAll(recommendedMovies2);

        List<MovieItem> differences21 = new ArrayList<>(recommendedMovies2);
        differences21.removeAll(recommendedMovies1);

        assertTrue("there's at least one difference between two recommendation lists", !differences12.isEmpty() || !differences21.isEmpty() );
    }
}