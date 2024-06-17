package com.group7.recommenderapp.util;

import com.google.common.hash.Hashing;
import com.group7.recommenderapp.entities.User;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class UserUtils {
    private static final String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

    public static boolean isValidEmail(String userName) {
        return pattern.matcher(userName).matches();
    }

    public static String generateUserDocId(String userName) {
        return Integer.toHexString(Hashing.murmur3_128().hashString(userName.toLowerCase(), StandardCharsets.UTF_8).asInt());
    }

    public static String generateUserProfileDocId(String username, String userDocId) {
        return username+userDocId;
    }

    public static boolean isCorrectPassword(User user, String givenPassword) {
        return user.getPassword().equals(givenPassword);
    }

}
