package com.group7.recommenderapp.service;

import com.group7.recommenderapp.entities.ContentItem;

import java.io.IOException;
import java.util.List;

public interface RecommenderService<T extends ContentItem> {

    void load();

    void unload();

    List<String> getGenresForRecommend() throws IOException;

    List<T> recommendByGenre(List<String> genres);

    List<T> recommendByItem(List<T> items);

}
