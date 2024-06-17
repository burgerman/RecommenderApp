package com.group7.recommenderapp.service;

import android.content.Context;

import com.group7.recommenderapp.service.RecommenderService;
import com.group7.recommenderapp.util.FileUtil;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieBasedRecommender implements RecommenderService {

    private static Logger LOG = Logger.getLogger(MovieBasedRecommender.class.getName());

    private static final String MODEL_DEFAULT_PATH = "";
    private Interpreter modelInterpreter;

    private Context context;

    private static RecommenderService movieRecommenderInstance = null;

    private MovieBasedRecommender(Context context) {
        this.context = context;
    }

    public static RecommenderService getInstance(Context ctx) {
        if (movieRecommenderInstance == null) {
            synchronized (MusicBasedRecommender.class) {
                if(movieRecommenderInstance == null) {
                    movieRecommenderInstance = new MovieBasedRecommender(ctx);
                }
            }
        }
        return movieRecommenderInstance;
    }

    @Override
    public void loadModel() {
        try{
            ByteBuffer buffer = FileUtil.loadModelFile(context.getAssets(), MODEL_DEFAULT_PATH);
            modelInterpreter = new Interpreter(buffer);
            LOG.info("Music Model loaded");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }

    }

    @Override
    public void unload() {
        if(modelInterpreter!=null) modelInterpreter.close();
    }

    @Override
    public List<String> rocommend(String category) {
        return null;
    }
}
