package com.group7.recommenderapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class AdditionalPreference implements Parcelable {
    private String genre;

    public AdditionalPreference(String genre) {
        this.genre = genre;
    }

    protected AdditionalPreference(Parcel in) {
        genre = in.readString();
    }

    public static final Creator<AdditionalPreference> CREATOR = new Creator<AdditionalPreference>() {
        @Override
        public AdditionalPreference createFromParcel(Parcel in) {
            return new AdditionalPreference(in);
        }

        @Override
        public AdditionalPreference[] newArray(int size) {
            return new AdditionalPreference[size];
        }
    };

    public String getGenre() {
        return genre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(genre);
    }
}
