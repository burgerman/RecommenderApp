package com.group7.recommenderapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.common.hash.Hashing;
import com.group7.recommenderapp.entities.User;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class UserUtils {
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(emailRegex);
    private static final String PREFS_NAME = "UserPreferences";
    private static final String MOVIE_PREFERENCES_KEY = "movie_preferences";

    public static boolean isValidEmail(String email) {
        return email != null && pattern.matcher(email).matches();
    }

    public static String generateUserDocId(String usernameOrEmail) {
        if (usernameOrEmail == null) {
            throw new IllegalArgumentException("Username or email cannot be null");
        }
        return usernameOrEmail.toLowerCase().replaceAll("\\s+", "") + "_doc";
    }

    public static String generateUserProfileDocId(String username, String userDocId) {
        return username + userDocId;
    }

    public static boolean isCorrectPassword(User user, String givenPassword) {
        return user.getPassword().equals(hashPassword(givenPassword));
    }

    public static String hashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{3,}$");
    }

    public static String generateDocId(String input) {
        return Hashing.murmur3_128().hashString(input, StandardCharsets.UTF_8).toString();
    }

    public static void saveUserMoviePreferences(Context context, List<String> preferences) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MOVIE_PREFERENCES_KEY, String.join(",", preferences));
        editor.apply();
    }

    public static List<String> getUserMoviePreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String preferencesString = prefs.getString(MOVIE_PREFERENCES_KEY, "");
        if (preferencesString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(preferencesString.split(","));
    }
}
