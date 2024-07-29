package com.group7.recommenderapp.profile;

import android.content.Context;
import android.util.Log;
import com.couchbase.lite.Blob;
import com.group7.recommenderapp.dao.UserProfileDao;
import com.group7.recommenderapp.entities.UserProfile;
import com.group7.recommenderapp.util.DatabaseManager;
import java.util.HashMap;
import java.util.Map;

public class UserProfilePresenter implements UserProfileContract.UserActionsListener {

    private static final String TAG = "UserProfilePresenter";
    private final UserProfileContract.View mView;
    private final Context context;
    private final UserProfileDao userProfileDao;

    public UserProfilePresenter(UserProfileContract.View view, Context context) {
        this.mView = view;
        this.context = context;
        this.userProfileDao = new UserProfileDao(DatabaseManager.getSharedInstance(context).getProfileCollection());
    }

    @Override
    public void fetchProfile() {
        String currentUserDocId = DatabaseManager.getSharedInstance(context).getCurrentUserDocId();
        UserProfile userProfile = userProfileDao.getUserProfile(currentUserDocId);

        if (userProfile != null) {
            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("uniqueId", userProfile.getUniqueId());
            profileMap.put("name", userProfile.getName());
            profileMap.put("age", userProfile.getAge());
            profileMap.put("gender", userProfile.getGender());
            profileMap.put("email", userProfile.getEmail());

            if (userProfile.getPreferences() != null) {
                profileMap.put("class1", userProfile.getPreferences().getClass1());
                profileMap.put("class2", userProfile.getPreferences().getClass2());
            }

            profileMap.put("likedItems", userProfile.getLikedItems());
            profileMap.put("imageData", userProfile.getImageData());

            mView.showProfile(profileMap);
        } else {
            Log.e(TAG, "User profile not found for current user");
            mView.showError("Failed to load user profile. Please try again.");
        }
    }

    @Override
    public void saveProfile(Map<String, Object> profile) {
        String currentUserDocId = DatabaseManager.getSharedInstance(context).getCurrentUserDocId();
        UserProfile userProfile = userProfileDao.getUserProfile(currentUserDocId);

        if (userProfile == null) {
            userProfile = new UserProfile(currentUserDocId);
        }

        userProfile.setName((String) profile.get("name"));
        userProfile.setAge((Integer) profile.get("age"));
        userProfile.setGender((String) profile.get("gender"));
        userProfile.setEmail((String) profile.get("email"));

        Blob imageData = (Blob) profile.get("imageData");
        if (imageData != null) {
            userProfile.setImageData(imageData);
        }

        boolean success = userProfileDao.createOrUpdateProfile(userProfile);

        if (success) {
            Log.d(TAG, "Profile updated successfully");
            mView.showSuccess("Profile updated successfully!");
        } else {
            Log.e(TAG, "Failed to update profile");
            mView.showError("Failed to update profile. Please try again.");
        }
    }
}
