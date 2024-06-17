package com.group7.recommenderapp.dao;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDictionary;
import com.couchbase.lite.MutableDocument;
import com.google.gson.Gson;
import com.group7.recommenderapp.entities.UserPreference;
import com.group7.recommenderapp.entities.UserProfile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileDao {
    private static final Logger LOGGER = Logger.getLogger(UserProfileDao.class.getName());
    private static final String docType = "user_profile";
    private static final Gson gson = new Gson();

    private String json;
    private Collection collection;

    public UserProfileDao(Collection collection) {
        this.collection = collection;
    }

    public void createOrUpdateProfile (UserProfile userProfile) {
        MutableDocument doc = new MutableDocument(userProfile.getUserDocumentId());
        doc.setString("type", docType);
        doc.setString("userDocumentId", userProfile.getUserDocumentId());
        doc.setString("gender", userProfile.getGender());
        doc.setInt("age", userProfile.getAge());
        if (userProfile.getPreferences() != null) {
            doc.setString("preferredLanguage", userProfile.getPreferences().getPreferredLanguage());
            doc.setString("class1", userProfile.getPreferences().getClass1());
            doc.setString("class2", userProfile.getPreferences().getClass2());
            if(userProfile.getPreferences().getPreferenceDict()!=null) {
                json = gson.toJson(userProfile.getPreferences().getPreferenceDict());
                MutableDictionary embededPrefDict = new MutableDictionary(json);
                doc.setDictionary("preferences", embededPrefDict);
            }
        }
        try{
            collection.save(doc);
        } catch (CouchbaseLiteException e) {
            LOGGER.log(Level.SEVERE, "can't save the user profile"+userProfile.getUserDocumentId(), e);
        }
    }

    public UserProfile getUserProfile(String documentId) {
        LOGGER.info("Fetching user profile for document ID: " + documentId);
        try{
        Document doc = collection.getDocument(documentId);
            if (doc != null) {
                UserProfile userProfile = new UserProfile(documentId);
                userProfile.setAge(doc.getInt("age"));
                userProfile.setGender(doc.getString("gender"));
                UserPreference preferences = null;
                preferences = new UserPreference(doc.getString("class1"));
                preferences.setPreferredLanguage(doc.getString("preferredLanguage"));
                preferences.setClass2(doc.getString("class2"));

                MutableDictionary preferencesDict = doc.toMutable().getDictionary("preferences");
                if (preferencesDict != null) {
                    preferences.setPreferenceDict(preferencesDict.toMap());
                }
                userProfile.setPreferences(preferences);
                return userProfile;
            } else {
                LOGGER.log(Level.SEVERE, "User profile not found for document ID: " + documentId);
            }
        } catch (CouchbaseLiteException e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch user profile for document ID: " + documentId, e);
        }
        return null;
    }

}
