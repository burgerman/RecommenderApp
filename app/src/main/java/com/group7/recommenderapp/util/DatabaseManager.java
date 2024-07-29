package com.group7.recommenderapp.util;

import android.content.Context;
import android.util.Log;
import com.couchbase.lite.*;
import java.util.List;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static final String DATABASE_NAME = "userprofile";
    private static final String USER_COLLECTION = "user_collection";
    private static final String PROFILE_COLLECTION = "profile_collection";
    private static final String PREFERENCE_COLLECTION = "preference_collection";
    private static DatabaseManager instance = null;
    private static Database database;
    private Collection preferenceCollection;
    private Collection userCollection;
    private Collection profileCollection;
    private String currentUser;

    protected DatabaseManager(Context context) {
        CouchbaseLite.init(context);
        DatabaseConfiguration config = new DatabaseConfiguration();
        try {
            database = new Database(DATABASE_NAME, config);
            userCollection = getOrCreateCollection(USER_COLLECTION);
            profileCollection = getOrCreateCollection(PROFILE_COLLECTION);
            preferenceCollection = getOrCreateCollection(PREFERENCE_COLLECTION);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error initializing database or collections", e);
        }
    }

    public static DatabaseManager getSharedInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(context);
                }
            }
        }
        return instance;
    }

    public Collection getUserCollection() {
        return userCollection;
    }

    public Collection getProfileCollection() {
        return profileCollection;
    }

    public Collection getPreferenceCollection() {
        return preferenceCollection;
    }

    public static Database getDatabase() {
        return database;
    }

    public String getCurrentUserDocId() {
        if (currentUser == null) {
            throw new IllegalStateException("Current user is not set");
        }
        return UserUtils.generateUserDocId(currentUser);
    }

    private Collection getOrCreateCollection(String collectionName) throws CouchbaseLiteException {
        Scope defaultScope = database.getDefaultScope();
        Collection collection = defaultScope.getCollection(collectionName);
        if (collection == null) {
            collection = database.createCollection(collectionName);
        }
        return collection;
    }

    public void closeDatabaseForUser() {
        try {
            if (database != null) {
                database.close();
                database = null;
            }
            currentUser = null;
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error closing database", e);
        }
    }

    public void saveMusicPreferences(List<String> musicPreferences) {
        savePreferences("music_preferences_", musicPreferences);
    }

    public void saveMoviePreferences(List<String> moviePreferences) {
        savePreferences("movie_preferences_", moviePreferences);
    }

    private void savePreferences(String prefix, List<String> preferences) {
        if (currentUser == null) {
            Log.e(TAG, "No current user set. Cannot save preferences.");
            return;
        }

        String docId = prefix + getCurrentUserDocId();
        MutableDocument document = new MutableDocument(docId);
        MutableArray array = new MutableArray();
        for (String preference : preferences) {
            array.addString(preference);
        }
        document.setArray("preferences", array);

        try {
            preferenceCollection.save(document);
            Log.i(TAG, prefix + "saved successfully for user: " + currentUser);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error saving " + prefix + e.getMessage());
        }
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
