package com.group7.recommenderapp.service;

import android.content.Context;

import com.group7.recommenderapp.entities.MusicItem;
import com.group7.recommenderapp.util.FileUtil;
import org.tensorflow.lite.Interpreter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicRecommender implements RecommenderService<MusicItem> {

    private static final Logger LOG = Logger.getLogger(MusicRecommender.class.getName());

    private static final String MODEL_DEFAULT_PATH = "";
    private Interpreter modelInterpreter;

    private final Context context;

    private static RecommenderService musicRecommenderInstance = null;

    private MusicRecommender(Context context) {
        this.context = context;
    }

    public static RecommenderService getInstance(Context ctx) {
        synchronized (MusicRecommender.class) {
            if(musicRecommenderInstance == null) {
                musicRecommenderInstance = new MusicRecommender(ctx);
            }
        }
        return musicRecommenderInstance;
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
        if(modelInterpreter!=null) modelInterpreter.close();
    }

    @Override
    public List<MusicItem> recommendByGenre(List<String> genres) {
        return Collections.emptyList();
    }

    @Override
    public List<MusicItem> recommendByItem(List<MusicItem> items) {
        return Collections.emptyList();
    }


    private void loadModel() {
        try{
            ByteBuffer buffer = FileUtil.loadModelFile(context.getAssets(), MODEL_DEFAULT_PATH);
            modelInterpreter = new Interpreter(buffer);
            LOG.info("Music Model loaded");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }

    }



}
