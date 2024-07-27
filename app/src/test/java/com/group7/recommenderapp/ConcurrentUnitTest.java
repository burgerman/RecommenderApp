package com.group7.recommenderapp;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.text.TextUtils;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.entities.ResultSet;
import com.group7.recommenderapp.util.ThreadpoolUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ConcurrentUnitTest {

    @BeforeClass
    public static void setUpClass() {
        mockStatic(android.text.TextUtils.class);
        when(TextUtils.join(any(CharSequence.class), any(Iterable.class))).thenAnswer(invocation -> {
            CharSequence delimiter = invocation.getArgument(0);
            Iterable tokens = invocation.getArgument(1);
            StringBuilder sb = new StringBuilder();
            boolean firstTime = true;
            for (Object token : tokens) {
                if (firstTime) {
                    firstTime = false;
                } else {
                    sb.append(delimiter);
                }
                sb.append(token);
            }
            return sb.toString();
        });
    }

    @AfterClass
    public static void tearDown() {
        ThreadpoolUtil.endThreadpool();
    }
    @Test
    public void testProcessResults() {
        ResultSet rs = new ResultSet();
        ContentItem item1 = new MovieItem();
        item1.setId(1);
        item1.setTitle("test movie1");
        item1.setGenres(Arrays.asList("genre1", "genre2"));
        item1.setSelected(true);
        item1.setScore(2);
        item1.setConfidence(77.3f);
        ContentItem item2 = new MovieItem();
        item2.setId(2);
        item2.setTitle("test movie2");
        item2.setGenres(Arrays.asList("genre1", "genre2"));
        item2.setSelected(true);
        item2.setScore(4);
        item2.setConfidence(67.8f);
        ContentItem item3 = new MovieItem();
        item3.setId(3);
        item3.setTitle("test movie3");
        item3.setGenres(Arrays.asList("genre1", "genre2"));
        item3.setSelected(true);
        item3.setScore(5);
        item3.setConfidence(85.3f);
        ContentItem item4 = new MovieItem();
        item4.setId(4);
        item4.setTitle("test movie4");
        item4.setGenres(Arrays.asList("genre1", "genre2"));
        item4.setSelected(true);
        item4.setScore(1);
        item4.setConfidence(90.2f);
        List<ContentItem> itemList = Arrays.asList(item1, item2, item3, item4);
        rs.setItemset(itemList);
        rs.setContentClass("Movie");

        ResultSet rs2 = new ResultSet();
        ContentItem music_item1 = new MovieItem();
        music_item1.setId(5);
        music_item1.setTitle("test music1");
        music_item1.setGenres(Arrays.asList("genre1", "genre2"));
        music_item1.setSelected(true);
        music_item1.setScore(3);
        music_item1.setConfidence(70.3f);
        ContentItem music_item2 = new MovieItem();
        music_item2.setId(6);
        music_item2.setTitle("test music2");
        music_item2.setGenres(Arrays.asList("genre1", "genre2"));
        music_item2.setSelected(true);
        music_item2.setScore(4);
        music_item2.setConfidence(65.8f);
        ContentItem music_item3 = new MovieItem();
        music_item3.setId(7);
        music_item3.setTitle("test music3");
        music_item3.setGenres(Arrays.asList("genre1", "genre2"));
        music_item3.setSelected(true);
        music_item3.setScore(5);
        music_item3.setConfidence(91.0f);
        ContentItem music_item4 = new MovieItem();
        music_item4.setId(8);
        music_item4.setTitle("test music4");
        music_item4.setGenres(Arrays.asList("genre1", "genre2"));
        music_item4.setSelected(true);
        music_item4.setScore(5);
        music_item4.setConfidence(96.2f);
        List<ContentItem> itemList2 = Arrays.asList(music_item1, music_item2, music_item3, music_item4);
        rs2.setItemset(itemList2);
        rs2.setContentClass("Music");
        List<ResultSet> resultSets = Arrays.asList(rs, rs2);
        List<List<ContentItem>> results = ThreadpoolUtil.processResults(resultSets);
        Assert.assertEquals(2, results.size());
        System.out.println("Results:");
        for (List<ContentItem> result : results) {
            System.out.println(result);
        }
    }
}