package com.group7.recommenderapp.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.service.MovieConfig;

public class FileUtil {

    /** Load TF Lite model from asset file. */
    public static MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath)
            throws IOException {
        try (AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
             FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor())) {
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    /** Load candidates from asset file. */
    public static Collection<MovieItem> loadMovieList(
            AssetManager assetManager, String candidateListPath) throws IOException {
        String content = loadFileContent(assetManager, candidateListPath);
        Gson gson = new Gson();
        Type type = new TypeToken<Collection<MovieItem>>() {}.getType();
        return gson.fromJson(content, type);
    }

    public static List<String> loadGenreList(AssetManager assetManager, String genreListPath)
            throws IOException {
        String content = loadFileContent(assetManager, genreListPath);
        String[] lines = content.split(System.lineSeparator());
        return Arrays.asList(lines);
    }

    /** Load config from asset file. */
    public static MovieConfig loadMovieConfig(AssetManager assetManager, String configPath) throws IOException {
        String content = loadFileContent(assetManager, configPath);
        Gson gson = new Gson();
        Type type = new TypeToken<MovieConfig>() {}.getType();
        return gson.fromJson(content, type);
    }

    /** Load file content from asset file. */
    @SuppressWarnings("AndroidJdkLibsChecker")
    private static String loadFileContent(AssetManager assetManager, String path) throws IOException {
        try (InputStream ins = assetManager.open(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(ins, UTF_8))) {
            return reader.lines().collect(joining(System.lineSeparator()));
        }
    }
}
