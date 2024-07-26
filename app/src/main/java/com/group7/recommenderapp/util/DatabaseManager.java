package com.group7.recommenderapp.util;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.MutableArray;

import java.util.List;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    private static final String DB_USER = "";
    private static Database database;
    private static DatabaseManager instance = null;
    private ListenerToken listenerToken;
    public String currentUser = null;
    private static final String DATABASE_NAME = "userprofile";
    private static final String USER_COLLECTION = "user_collection";
    private static final String PROFILE_COLLECTION = "profile_collection";
    private static final String PREFERENCE_COLLECTION = "preference_collection";
    private Collection preferenceCollection;
    private Collection userCollection;
    private Collection profileCollection;

    protected DatabaseManager(Context context) {
        CouchbaseLite.init(context);
        DatabaseConfiguration config = new DatabaseConfiguration();
        try {
            database = new Database(DATABASE_NAME, config);
            userCollection = database.getDefaultScope().getCollection(USER_COLLECTION);
            profileCollection = database.getDefaultScope().getCollection(PROFILE_COLLECTION);
            preferenceCollection = database.getDefaultScope().getCollection(PREFERENCE_COLLECTION);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, e.getMessage());
        }
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

    public static Database getDatabase() {
        return database;
    }

    public String getCurrentUserDocId() {
        return UserUtils.generateUserDocId(currentUser);
    }

    public void openOrCreateDatabaseForUser(Context context, String userName) {
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir(), DB_USER));

        currentUser = userName;

        try {
            database = new Database(DATABASE_NAME, config);
            registerForDatabaseChanges();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private void registerForDatabaseChanges() {
        listenerToken = database.addChangeListener(change -> {
            if (change != null) {
                for (String docId : change.getDocumentIDs()) {
                    Document doc = database.getDocument(docId);
                    if (doc != null) {
                        Log.i("DatabaseChangeEvent", "Document has been inserted or updated");
                    }
                }
            }
        });
    }

    public void closeDatabaseForUser() {
        try {
            if (database != null) {
                deregisterForDatabaseChanges();
                database.close();
                database = null;
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private void deregisterForDatabaseChanges() {
        if (listenerToken != null) {
            database.removeChangeListener(listenerToken);
        }
    }

    public void saveMoviePreferences(List<String> moviePreferences) {
        if (currentUser == null) {
            Log.e(TAG, "No current user set. Cannot save movie preferences.");
            return;
        }

        String docId = "movie_preferences_" + getCurrentUserDocId();
        MutableDocument document = new MutableDocument(docId);

        // Convert List<String> to com.couchbase.lite.Array
        MutableArray array = new MutableArray();
        for (String preference : moviePreferences) {
            array.addString(preference);
        }

        document.setArray("preferences", array);

        try {
            preferenceCollection.save(document);
            Log.i(TAG, "Movie preferences saved successfully for user: " + currentUser);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error saving movie preferences: " + e.getMessage());
        }
    }

    public void saveMusicPreferences(List<String> musicPreferences) {
        if (currentUser == null) {
            Log.e(TAG, "No current user set. Cannot save music preferences.");
            return;
        }

        String docId = "music_preferences_" + getCurrentUserDocId();
        MutableDocument document = new MutableDocument(docId);

        // Convert List<String> to com.couchbase.lite.Array
        MutableArray array = new MutableArray();
        for (String preference : musicPreferences) {
            array.addString(preference);
        }

        document.setArray("preferences", array);

        try {
            preferenceCollection.save(document);
            Log.i(TAG, "Music preferences saved successfully for user: " + currentUser);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error saving music preferences: " + e.getMessage());
        }
    }
}
