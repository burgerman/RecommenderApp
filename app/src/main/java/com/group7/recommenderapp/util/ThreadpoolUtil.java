package com.group7.recommenderapp.util;

import com.group7.recommenderapp.entities.ContentItem;
import com.group7.recommenderapp.entities.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadpoolUtil {
    private static Logger LOG = Logger.getLogger(ThreadpoolUtil.class.getName());
    private static final int numberOfThreads = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);


    public static List<List<ContentItem>> processResults(List<ResultSet> resultSets) {
        synchronized (ThreadpoolUtil.class) {
            List<Future<List<ContentItem>>> futures = new ArrayList<>();
            for (ResultSet rs : resultSets) {
                futures.add(executorService.submit(new ResultSetJob(rs)));
            }
            List<List<ContentItem>> results = new ArrayList<>();
            for (Future<List<ContentItem>> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException i) {
                    LOG.log(Level.SEVERE, i.getMessage());
                }
            }
            return results;
        }
    }

    public static List<List<ContentItem>> processResults(List<ResultSet> resultSets, int topK) {
        synchronized (ThreadpoolUtil.class) {
            List<Future<List<ContentItem>>> futures = new ArrayList<>();
            for (ResultSet rs : resultSets) {
                futures.add(executorService.submit(new ResultSetJob(rs, topK)));
            }
            List<List<ContentItem>> results = new ArrayList<>();
            for (Future<List<ContentItem>> future : futures) {
                try {
                    results.add(future.get());
                } catch (InterruptedException | ExecutionException i) {
                    LOG.log(Level.SEVERE, i.getMessage());
                }
            }
            return results;
        }
    }

    public static void endThreadpool() {
        if (!executorService.isTerminated()) executorService.shutdown();
    }

}
