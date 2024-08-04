package com.group7.recommenderapp.entities;

import android.os.Parcel;

public class MusicItem extends ContentItem{
    private String artist;

    public MusicItem() {
    }

    protected MusicItem(Parcel in) {
        super(in);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MusicItem{ " +
                super.toString() +
                " artist='" + artist + '\'' +
                '}';
    }
}