package com.group7.recommenderapp.entities;

public class MovieItem extends ContentItem{

    private String director;

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
