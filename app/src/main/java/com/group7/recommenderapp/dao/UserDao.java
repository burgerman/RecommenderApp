package com.group7.recommenderapp.dao;

import android.util.Log;
import com.couchbase.lite.*;
import com.group7.recommenderapp.entities.User;

public class UserDao {
    private static final String TAG = "UserDao";
    private static final String DOC_TYPE = "user";
    private final Collection collection;

    public UserDao(Collection collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        this.collection = collection;
    }

    public boolean createOrUpdateUser(User user) {
        if (user == null || user.getDocumentId() == null) {
            Log.e(TAG, "Invalid user data");
            return false;
        }

        MutableDocument doc = new MutableDocument(user.getDocumentId());
        doc.setString("type", DOC_TYPE);
        doc.setString("username", user.getUserName());
        doc.setString("password", user.getPassword());
        doc.setString("email", user.getEmail());
        doc.setString("document_id", user.getDocumentId());

        try {
            collection.save(doc);
            Log.d(TAG, "User saved successfully: " + user.getUserName() + ", Email: " + user.getEmail());
            return true;
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Failed to save user: " + user.getUserName(), e);
        }
        return false;
    }

    public User getUser(String docId) {
        if (docId == null) {
            Log.e(TAG, "Document ID cannot be null");
            return null;
        }

        try {
            Document doc = collection.getDocument(docId);
            if (doc != null) {
                return documentToUser(doc);
            }
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Failed to fetch user with docId: " + docId, e);
        }
        return null;
    }

    public User getUserByUsername(String usernameOrEmail) {
        if (usernameOrEmail == null) {
            Log.e(TAG, "Username or email cannot be null");
            return null;
        }

        try {
            Query query = QueryBuilder.select(SelectResult.all())
                    .from(DataSource.collection(collection))
                    .where(Expression.property("type").equalTo(Expression.string(DOC_TYPE))
                            .and(Expression.property("username").equalTo(Expression.string(usernameOrEmail))
                                    .or(Expression.property("email").equalTo(Expression.string(usernameOrEmail)))));

            Result result = query.execute().next();
            if (result != null) {
                Log.d(TAG, "User found with username or email: " + usernameOrEmail);
                return dictionaryToUser(result.getDictionary(collection.getName()));
            } else {
                Log.d(TAG, "No user found with username or email: " + usernameOrEmail);
            }
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Failed to fetch user with username or email: " + usernameOrEmail, e);
        }
        return null;
    }

    private User documentToUser(Document doc) {
        User user = new User();
        user.setUserName(doc.getString("username"));
        user.setPassword(doc.getString("password"));
        user.setEmail(doc.getString("email"));
        user.setDocumentId(doc.getString("document_id"));
        return user;
    }

    private User dictionaryToUser(Dictionary dict) {
        User user = new User();
        user.setUserName(dict.getString("username"));
        user.setPassword(dict.getString("password"));
        user.setEmail(dict.getString("email"));
        user.setDocumentId(dict.getString("document_id"));
        return user;
    }

    public void createTestUser() {
        User testUser = new User("testuser", "testuser@example.com", "password123");
        boolean success = createOrUpdateUser(testUser);
        if (success) {
            Log.d(TAG, "Test user created successfully");
        } else {
            Log.e(TAG, "Failed to create test user");
        }
    }
}
