package com.group7.recommenderapp.service;

import java.util.List;

public interface RecommenderService {

    public void loadModel();

    public void unload();

    public List<String> rocommend(String category);

}
