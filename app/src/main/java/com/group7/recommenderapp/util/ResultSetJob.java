package com.group7.recommenderapp.util;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.ResultSet;

import java.util.List;
import java.util.concurrent.Callable;

public class ResultSetJob implements Callable<List<ContentItem>> {
    private ResultSet resultSet;
    private int k;

    public ResultSetJob(ResultSet resultSet) {
        this.resultSet = resultSet;
        this.k = ResultSet.DEFAULT_TOPK;
    }

    public ResultSetJob(ResultSet resultSet, int k) {
        this.resultSet = resultSet;
        this.k = k;
    }

    @Override
    public List<ContentItem> call() throws Exception {
        if(k!=ResultSet.DEFAULT_TOPK) {
            return resultSet.getTopK();
        }
        return resultSet.getTopK(k);
    }
}
