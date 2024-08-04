package com.group7.recommenderapp.entities;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenreEntity {
    public static final int DEFAULT_TOPK = 9;

    private List<String> genres;

    public List<String> getGenres() {
        if(genres!=null) {
            return genres;
        }
        return null;
    }

    public List<String> getTopKGenres(int k, List<String> genreList) {
        if(k != DEFAULT_TOPK) {
            Collections.shuffle(genreList);
            this.genres = genreList.stream().limit(k).collect(Collectors.toList());
        } else{
            this.genres = genreList.stream().limit(DEFAULT_TOPK).collect(Collectors.toList());
        }
        return genres;
    }

    public List<String> getTopKGenres(List<String> genreList) {
        Collections.shuffle(genreList);
        this.genres = genreList.stream().limit(DEFAULT_TOPK).collect(Collectors.toList());
        return genres;
    }
}