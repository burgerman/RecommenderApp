package com.group7.recommenderapp.profile;

import android.content.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;
import com.group7.recommenderapp.util.DatabaseManager;
import java.util.HashMap;
import java.util.Map;

public class UserProfilePresenter implements UserProfileContract.UserActionsListener {
    private UserProfileContract.View mUserProfileView;
    private Context context;

    public UserProfilePresenter(UserProfileContract.View mUserProfileView, Context context) {
        this.mUserProfileView = mUserProfileView;
        this.context = context;
    }

    public void fetchProfile() {
        Database database = DatabaseManager.getDatabase();
        String docId = DatabaseManager.getSharedInstance(context).getCurrentUserDocId();

        if (database != null) {
            Map<String, Object> profile = new HashMap<>();
            profile.put("email", DatabaseManager.getSharedInstance(context).currentUser);

            Document document = database.getDocument(docId);
            if (document != null) {
                profile.put("uniqueId", document.getString("uniqueId"));
                profile.put("name", document.getString("name"));
                profile.put("age", document.getNumber("age").intValue());
                profile.put("gender", document.getString("gender"));
                profile.put("class1", document.getString("class1"));
                profile.put("class2", document.getString("class2"));
                profile.put("likedItems", document.getArray("likedItems").toList());
                profile.put("imageData", document.getBlob("imageData"));
            }

            mUserProfileView.showProfile(profile);
        }
    }

    public void saveProfile(Map<String, Object> profile) {
        Database database = DatabaseManager.getDatabase();
        String docId = DatabaseManager.getSharedInstance(context).getCurrentUserDocId();
        MutableDocument mutableDocument = new MutableDocument(docId, profile);

        try {
            database.save(mutableDocument);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
