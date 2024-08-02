package com.group7.recommenderapp.util;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLite;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseChange;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.ListenerToken;
import com.couchbase.lite.Scope;

import java.util.concurrent.Executor;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    private static final String DB_USER = "user_db";
    private static Database database;
    private static DatabaseManager instance = null;
    private ListenerToken listenerToken;
    public  String currentUser = null;
    private static final String DATABASE_NAME = "userprofile";
    private static final String USER_COLLECTION = "user_collection";
    private static final String PROFILE_COLLECTION = "profile_collection";
    private Collection userCollection;
    private Collection profileCollection;

    protected DatabaseManager(Context context) {
        CouchbaseLite.init(context);
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir(), DB_USER));
        try {
            database = new Database(DATABASE_NAME, config);
            userCollection = database.getDefaultScope().getCollection(USER_COLLECTION);
            if(userCollection==null) {
                userCollection = database.createCollection(USER_COLLECTION, database.getDefaultScope().getName());
            }
            profileCollection = database.getDefaultScope().getCollection(PROFILE_COLLECTION);
            if(profileCollection==null) {
                profileCollection = database.createCollection(PROFILE_COLLECTION, database.getDefaultScope().getName());
            }
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

    public static DatabaseManager getSharedInstance(Context context) {
        if(instance == null) {
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
    public void setCurrentUser(String user)
    {
        this.currentUser = user;
    }
    private void registerForDatabaseChanges()
    {

        listenerToken = database.addChangeListener(change -> {
            if (change != null) {
                for(String docId : change.getDocumentIDs()) {
                    Document doc = database.getDocument(docId);
                    if (doc != null) {
                        Log.i("DatabaseChangeEvent", "Document has been inserted or updated");
                    }
                }
            }
        });
    }

    public void closeDatabaseForUser()
    {
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
    private void deregisterForDatabaseChanges()
    {
        if (listenerToken != null) {
            database.removeChangeListener(listenerToken);
        }
    }
}
