package com.group7.recommenderapp.profile;

import java.util.Map;

public interface UserProfileContract {

    interface View {

        void showProfile(Map<String, Object> profile);

    }

    interface UserActionsListener {

        void fetchProfile();

        void saveProfile(Map<String, Object> profile);

    }
}
