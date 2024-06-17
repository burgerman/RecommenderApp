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
    private static final String DB_USER = "";
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
        try {
            database = new Database(DATABASE_NAME, config);
            userCollection = database.getDefaultScope().getCollection(USER_COLLECTION);
            profileCollection = database.getDefaultScope().getCollection(PROFILE_COLLECTION);
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


    // tag::userProfileDocId[]
    public String getCurrentUserDocId() {
        return UserUtils.generateUserDocId(currentUser);
    }
    // end::userProfileDocId[]

    // tag::openOrCreateDatabase[]
    public void openOrCreateDatabaseForUser(Context context, String userName)
    // end::openOrCreateDatabase[]
    {
        // tag::databaseConfiguration[]
        DatabaseConfiguration config = new DatabaseConfiguration();
        config.setDirectory(String.format("%s/%s", context.getFilesDir(), DB_USER));
        // end::databaseConfiguration[]

        currentUser = userName;

        try {
            // tag::createDatabase[]
            database = new Database(DATABASE_NAME, config);
            // end::createDatabase[]
            registerForDatabaseChanges();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    // tag::registerForDatabaseChanges[]
    private void registerForDatabaseChanges()
    // end::registerForDatabaseChanges[]
    {
        // tag::addDatabaseChangelistener[]
        // Add database change listener
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
        // end::addDatabaseChangelistener[]
    }

    // tag::closeDatabaseForUser[]
    public void closeDatabaseForUser()
    // end::closeDatabaseForUser[]
    {
        try {
            if (database != null) {
                deregisterForDatabaseChanges();
                // tag::closeDatabase[]
                database.close();
                // end::closeDatabase[]
                database = null;
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    // tag::deregisterForDatabaseChanges[]
    private void deregisterForDatabaseChanges()
    // end::deregisterForDatabaseChanges[]
    {
        if (listenerToken != null) {
            // tag::removedbchangelistener[]
            database.removeChangeListener(listenerToken);
            // end::removedbchangelistener[]
        }
    }
}
