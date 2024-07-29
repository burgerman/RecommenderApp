package com.group7.recommenderapp.entities;

import com.couchbase.lite.Blob;
import java.util.List;

public class UserProfile {
    private String userDocumentId;
    private String uniqueId;
    private String name;
    private int age;
    private String gender;
    private String email;
    private String preferredLanguage;
    private String class1;
    private String class2;
    private UserPreference preferences;
    private List<String> likedItems;
    private Blob imageData;

    // Default constructor
    public UserProfile() {}

    // Constructor with userDocumentId
    public UserProfile(String userDocumentId) {
        this.userDocumentId = userDocumentId;
    }

    // Getters and setters
    public String getUserDocumentId() {
        return userDocumentId;
    }

    public void setUserDocumentId(String userDocumentId) {
        this.userDocumentId = userDocumentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

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

    public Blob getImageData() {
        return imageData;
    }

    public void setImageData(Blob imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userDocumentId='" + userDocumentId + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", preferredLanguage='" + preferredLanguage + '\'' +
                ", class1='" + class1 + '\'' +
                ", class2='" + class2 + '\'' +
                ", preferences=" + preferences +
                ", likedItems=" + likedItems +
                ", imageData=" + (imageData != null ? "present" : "null") +
                '}';
    }
}
