package com.group7.recommenderapp.entities;

public class MusicItem extends ContentItem{
    private String artist;

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