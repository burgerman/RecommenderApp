package com.group7.recommenderapp.entities;

import java.util.List;

public class UserProfile {

    private String userDocumentId;
    private String uniqueId;
    private String name;
    private int age;
    private String gender;
    private UserPreference preferences;
    private List<String> likedItems;

    public UserProfile(String userDocumentId) {
        this.userDocumentId = userDocumentId;
    }

    public String getUserDocumentId() {
        return userDocumentId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserPreference getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreference preferences) {
        this.preferences = preferences;
    }

    public List<String> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(List<String> likedItems) {
        this.likedItems = likedItems;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "uniqueId='" + uniqueId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", preferences=" + preferences +
                ", likedItems=" + likedItems +
                '}';
    }
}
