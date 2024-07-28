package com.group7.recommenderapp.dao;

import com.couchbase.lite.Array;
import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.group7.recommenderapp.entities.UserPreference;
import com.group7.recommenderapp.entities.UserProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileDao {
    private static final Logger LOGGER = Logger.getLogger(UserProfileDao.class.getName());
    private static final String docType = "user_profile";
    private Collection collection;

    public UserProfileDao(Collection collection) {
        this.collection = collection;
    }

    public void createOrUpdateProfile(UserProfile userProfile) {
        MutableDocument doc = new MutableDocument(userProfile.getUserDocumentId());
        doc.setString("type", docType);
        doc.setString("userDocumentId", userProfile.getUserDocumentId());
        doc.setString("uniqueId", userProfile.getUniqueId());
        doc.setString("name", userProfile.getName());
        doc.setInt("age", userProfile.getAge());
        doc.setString("gender", userProfile.getGender());
        if (userProfile.getPreferences() != null) {
            doc.setString("class1", userProfile.getPreferences().getClass1());
            doc.setString("class2", userProfile.getPreferences().getClass2());
        }
        if (userProfile.getLikedItems() != null) {
            MutableArray likedItemsArray = new MutableArray();
            for (String item : userProfile.getLikedItems()) {
                likedItemsArray.addString(item);
            }
            doc.setArray("likedItems", likedItemsArray);
        }
        try {
            collection.save(doc);
        } catch (CouchbaseLiteException e) {
            LOGGER.log(Level.SEVERE, "can't save the user profile" + userProfile.getUserDocumentId(), e);
        }
    }

    public UserProfile getUserProfile(String documentId) {
        LOGGER.info("Fetching user profile for document ID: " + documentId);
        try {
            Document doc = collection.getDocument(documentId);
            if (doc != null) {
                UserProfile userProfile = new UserProfile(documentId);
                userProfile.setUniqueId(doc.getString("uniqueId"));
                userProfile.setName(doc.getString("name"));
                userProfile.setAge(doc.getInt("age"));
                userProfile.setGender(doc.getString("gender"));
                UserPreference preferences = new UserPreference(doc.getString("class1"));
                preferences.setClass2(doc.getString("class2"));
                userProfile.setPreferences(preferences);

                Array likedItemsArray = doc.getArray("likedItems");
                if (likedItemsArray != null) {
                    List<String> likedItems = new ArrayList<>();
                    for (int i = 0; i < likedItemsArray.count(); i++) {
                        likedItems.add(likedItemsArray.getString(i));
                    }
                    userProfile.setLikedItems(likedItems);
                }

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
