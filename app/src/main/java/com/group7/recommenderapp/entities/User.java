package com.group7.recommenderapp.entities;

public class User {

    private String userName;
    private String password;
    private String documentId;
    private UserProfile profile;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", profile=" + profile +
                '}';
    }
}
