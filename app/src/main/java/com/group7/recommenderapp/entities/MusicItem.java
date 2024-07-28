package com.group7.recommenderapp.entities;

import java.util.List;

public class MusicItem extends ContentItem {
    private String artist;

    public MusicItem(int id, String title, List<String> genres, int score, String artist) {
        super(id, title, genres, score);
        this.artist = artist;
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
