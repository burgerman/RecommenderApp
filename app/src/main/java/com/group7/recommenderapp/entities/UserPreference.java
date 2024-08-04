package com.group7.recommenderapp.entities;

import java.util.List;
import java.util.Map;

public class UserPreference {
    private String preferredLanguage;
    private String class1;
    private String class2;
    // out key - class, inner key - category, value - content list
    private Map<String, Object> preferenceDict;

    public UserPreference(String class1) {
        this.class1 = class1;
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

    public Map<String, Object> getPreferenceDict() {
        return preferenceDict;
    }

    public void setPreferenceDict(Map<String, Object> preferenceDict) {
        this.preferenceDict = preferenceDict;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "preferredLanguage='" + preferredLanguage + '\'' +
                ", class1='" + class1 + '\'' +
                ", class2='" + class2 +
                '}';
    }
}