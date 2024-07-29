package com.group7.recommenderapp.entities;

import java.util.List;

public class UserPreference {
    private String class1;
    private String class2;
    private String preferredLanguage;
    private List<String> categoriesClass1;
    private List<String> categoriesClass2;
    private List<String> likedMovieList;
    private List<String> likedMusicList;
    private String additionalPreference;

    // Default constructor
    public UserPreference() {}

    // Constructor with class1
    public UserPreference(String class1) {
        this.class1 = class1;
    }

    // Getters and setters
    public String getClass1() {
        return class1;
    }

    public void setClass1(String class1) {
        this.class1 = class1;
    }

    public String getClass2() {
        return class2;
    }

    public void setClass2(String class2) {
        this.class2 = class2;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public List<String> getCategoriesClass1() {
        return categoriesClass1;
    }

    public void setCategoriesClass1(List<String> categoriesClass1) {
        this.categoriesClass1 = categoriesClass1;
    }

    public List<String> getCategoriesClass2() {
        return categoriesClass2;
    }

    public void setCategoriesClass2(List<String> categoriesClass2) {
        this.categoriesClass2 = categoriesClass2;
    }

    public List<String> getLikedMovieList() {
        return likedMovieList;
    }

    public void setLikedMovieList(List<String> likedMovieList) {
        this.likedMovieList = likedMovieList;
    }

    public List<String> getLikedMusicList() {
        return likedMusicList;
    }

    public void setLikedMusicList(List<String> likedMusicList) {
        this.likedMusicList = likedMusicList;
    }

    public String getAdditionalPreference() {
        return additionalPreference;
    }

    public void setAdditionalPreference(String additionalPreference) {
        this.additionalPreference = additionalPreference;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "class1='" + class1 + '\'' +
                ", class2='" + class2 + '\'' +
                ", preferredLanguage='" + preferredLanguage + '\'' +
                ", categoriesClass1=" + categoriesClass1 +
                ", categoriesClass2=" + categoriesClass2 +
                ", likedMovieList=" + likedMovieList +
                ", likedMusicList=" + likedMusicList +
                ", additionalPreference='" + additionalPreference + '\'' +
                '}';
    }
}
