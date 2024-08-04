package com.group7.recommenderapp.entities;

public class UserProfile {

    private String userDocumentId;
    private String gender;
    private int age;
    private UserPreference preferences;

    public UserProfile(String userDocumentId) {
        this.userDocumentId = userDocumentId;
    }

    public String getUserDocumentId() {
        return userDocumentId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserPreference getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreference preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "gender='" + gender + '\'' +
                ", age=" + age +
                ", preferences=" + preferences +
                '}';
    }
}