package com.group7.recommenderapp.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.group7.recommenderapp.entities.MovieItem;
import com.group7.recommenderapp.service.MovieConfig;

public class FileUtil {
    public static final String MOVIE_CONTENT_FILE_URL = "http://10.0.2.2:8000/sorted_movie_vocab.json";
    public static final String MUSIC_CONTENT_FILE_URL = "http://10.0.2.2:8000/sorted_music_vocab.json";
    public static final String LOCAL_MOVIE_CONTENT_FILE_NAME = "sorted_movie_vocab.json";
    public static final String LOCAL_MUSIC_CONTENT_FILE_NAME = "sorted_music_vocab.json";
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

    public static class DownloadFileTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String fileUrl = params[0];   // URL to download file from
            String fileName = params[1];  // Name to save the file as

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                // Create a file output stream to save the downloaded file
                FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                byte[] data = new byte[1024];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, count);
                }

                // Flush and close streams
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

                return "Download complete";
            } catch (Exception e) {
                Log.e("FileUtil", "Failed to download the file:" +fileName);
                return "Download failed";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("FileUtil", "Download Result: "+result);
        }
    }
}