package com.group7.recommenderapp;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.service.MusicRecommender;
import com.group7.recommenderapp.service.MusicConfig;
import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.util.FileUtil;

import org.junit.After;
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
public class MusicRecommenderServiceTest {
    private static final String TAG = "MusicRecommenderServiceTest";

    private static RecommenderService musicRecommender;
    private static final int RANDOM_BOUNDS = 10;
    private Context ctx;
    private final Random rand = new Random();

    @Before
    public void setUp() {
        ctx = InstrumentationRegistry.getTargetContext();

        try {
            musicRecommender = MusicRecommender.getInstance(ctx);
            musicRecommender.load();
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error occurs when loading MusicRecommender config."), ex);
        }
    }

    @After
    public void tearDown() {
        musicRecommender.unload();
    }

    @Test
    public void musicRecommendByGenreTest() throws IOException {
        // get genre list from assets file
        List<String> genres =  musicRecommender.getGenresForRecommend();

        // randomly select some genres from asset file
        Collections.shuffle(genres);
        List<String> selectedGenres = genres.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedGenres1: " + selectedGenres);

        // assert the recommendation list is non-empty and correct in terms of length, rating/score and genre
        List<ContentItem> recommendedMusic1 = musicRecommender.recommendByGenre(selectedGenres);
        Log.i(TAG, "recommendedMusics1: " + recommendedMusic1);


        assertTrue("The recommendation list is not empty", recommendedMusic1.size() > 0);
        float lastScore = 10000;
        float newScore = 0;
        for (ContentItem item : recommendedMusic1) {
//            assertTrue("genres of recommendedMusics is contained in user's selection", genres.contains(item.getGenres()));
            newScore = item.getScore();

            assertTrue("score of recommendedMusic is in decreasing order", lastScore >= newScore);
            lastScore = newScore;
        }

        // randomly select another few genres
        Collections.shuffle(genres);
        selectedGenres = genres.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedGenres2: " + selectedGenres);

        // assert the recommendation list is different from previous one
        List<ContentItem> recommendedMusic2 = musicRecommender.recommendByGenre(selectedGenres);
        Log.i(TAG, "recommendedMusic2: " + recommendedMusic1);

        List<ContentItem> differences12 = new ArrayList<>(recommendedMusic1);
        differences12.removeAll(recommendedMusic2);

        List<ContentItem> differences21 = new ArrayList<>(recommendedMusic2);
        differences21.removeAll(recommendedMusic1);

        assertTrue("there's at least one difference between two recommendation lists", !differences12.isEmpty() || !differences21.isEmpty());
    }

    @Test
    public void musicRecommendByItemTest() throws IOException {
        // get music list from assets file
        List<MusicItem> music = (List<MusicItem>) FileUtil.loadMusicList(ctx.getAssets(), "sorted_music_vocab.json");

        // randomly select some genres from asset file
        Collections.shuffle(music);
        List<MusicItem> selectedMusic = music.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedMusic1: " + selectedMusic);

        // assert the recommendation list is non-empty and correct in terms of length, rating/score and genre
        List<MusicItem> recommendedMusic1 = musicRecommender.recommendByItem(selectedMusic);
        Log.i(TAG, "recommendedMusic1: " + recommendedMusic1);

        assertTrue("The recommendation list is not empty", !recommendedMusic1.isEmpty());
        float lastConfidence = 1;
        float newConfidence = 0;
        for (MusicItem item : recommendedMusic1) {
            newConfidence = item.getConfidence();

            assertTrue("confidence of recommendedMusic is in decreasing order", lastConfidence >= newConfidence);
            lastConfidence = newConfidence;
        }

        // randomly select another few items
        Collections.shuffle(music);
        selectedMusic = music.subList(0, rand.nextInt(RANDOM_BOUNDS)+1);
        Log.i(TAG, "selectedMusic2: " + selectedMusic);

        // assert the recommendation list is different from previous one
        List recommendedMusic2 = musicRecommender.recommendByItem(selectedMusic);
        Log.i(TAG, "recommendedMusic2: " + recommendedMusic1);

        List<MusicItem> differences12 = new ArrayList<>(recommendedMusic1);
        differences12.removeAll(recommendedMusic2);

        List<MusicItem> differences21 = new ArrayList<>(recommendedMusic2);
        differences21.removeAll(recommendedMusic1);

        assertTrue("there's at least one difference between two recommendation lists", !differences12.isEmpty() || !differences21.isEmpty() );
    }
}