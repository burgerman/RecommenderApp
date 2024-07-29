package com.group7.recommenderapp.dao;

import android.util.Log;
import com.couchbase.lite.Array;
import com.couchbase.lite.Blob;
import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.group7.recommenderapp.entities.UserPreference;
import com.group7.recommenderapp.entities.UserProfile;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDao {
    private static final String TAG = "UserProfileDao";
    private static final String DOC_TYPE = "user_profile";
    private final Collection collection;

    public UserProfileDao(Collection collection) {
        this.collection = collection;
    }

    public boolean createOrUpdateProfile(UserProfile userProfile) {
        MutableDocument doc = new MutableDocument(userProfile.getUserDocumentId());
        doc.setString("type", DOC_TYPE);
        doc.setString("userDocumentId", userProfile.getUserDocumentId());
        doc.setString("uniqueId", userProfile.getUniqueId());
        doc.setString("name", userProfile.getName());
        doc.setInt("age", userProfile.getAge());
        doc.setString("gender", userProfile.getGender());
        doc.setString("email", userProfile.getEmail());
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
        if (userProfile.getImageData() != null) {
            doc.setBlob("imageData", userProfile.getImageData());
        }
        try {
            collection.save(doc);
            return true;
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Can't save the user profile " + userProfile.getUserDocumentId(), e);
            return false;
        }
    }

    public UserProfile getUserProfile(String documentId) {
        Log.i(TAG, "Fetching user profile for document ID: " + documentId);
        try {
            Document doc = collection.getDocument(documentId);
            if (doc != null) {
                UserProfile userProfile = new UserProfile(documentId);
                userProfile.setUniqueId(doc.getString("uniqueId"));
                userProfile.setName(doc.getString("name"));
                userProfile.setAge(doc.getInt("age"));
                userProfile.setGender(doc.getString("gender"));
                userProfile.setEmail(doc.getString("email"));
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

                Blob imageData = doc.getBlob("imageData");
                if (imageData != null) {
                    userProfile.setImageData(imageData);
                }

                return userProfile;
            } else {
                Log.e(TAG, "User profile not found for document ID: " + documentId);
            }
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Failed to fetch user profile for document ID: " + documentId, e);
        }
        return null;
    }
}
