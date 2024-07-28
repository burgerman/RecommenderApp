package com.group7.recommenderapp.entities;

import java.util.List;

public class MovieItem extends ContentItem {
    private String director;

    public MovieItem(int id, String title, List<String> genres, int score, String director) {
        super();  // Call the default constructor of ContentItem
        setId(id);
        setTitle(title);
        setGenres(genres);
        setScore(score);
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return "MovieItem{ " +
                super.toString() +
                " director='" + director + '\'' +
                '}';
    }
}
