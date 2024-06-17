package com.group7.recommenderapp.dao;

import android.util.Log;
import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;
import com.group7.recommenderapp.entities.User;
import com.group7.recommenderapp.util.UserUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());
    private static final String docType = "user";
    private Collection collection;

    public UserDao(Collection collection) {
        this.collection = collection;
    }

    public boolean createOrUpdateUser(User u) {
        MutableDocument doc = new MutableDocument(u.getDocumentId());
        doc.setString("type", docType);
        doc.setString("username", u.getUserName());
        doc.setString("password", u.getPassword());
        doc.setString("document_id", u.getDocumentId());
        try{
            collection.save(doc);
            return collection.getDocument(doc.getId()) != null;
        } catch (CouchbaseLiteException e) {
            LOGGER.log(Level.SEVERE, "can't save new user", e);
        }
        return false;
    }

    public User getUser(String docId) {
        try{
            Document doc = collection.getDocument(docId);
            if(doc!=null) {
                User user = new User();
                user.setUserName(doc.getString("username"));
                user.setPassword(doc.getString("password"));
                user.setDocumentId(docId);
                return user;
            }
        } catch (CouchbaseLiteException e) {
            LOGGER.log(Level.SEVERE, "can't fetch the user "+docId, e);
        }
        return null;
    }


}
