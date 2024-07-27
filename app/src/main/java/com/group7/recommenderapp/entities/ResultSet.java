package com.group7.recommenderapp.entities;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ResultSet {

    public static final int DEFAULT_TOPK = 3;
    private String contentClass;
    private List<ContentItem> itemset;
    private List<ContentItem> topK;

    public String getContentClass() {
        return contentClass;
    }

    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }

    public List<ContentItem> getItemset() {
        return itemset;
    }

    public void setItemset(List<ContentItem> itemset) {
        this.itemset = itemset;
    }

    public List<ContentItem> getTopK(int k) {
        if(topK == null || k != DEFAULT_TOPK) {
            itemset.sort(Comparator.comparing(ContentItem::getScore, Comparator.reverseOrder())
                    .thenComparing(ContentItem::getConfidence, Comparator.reverseOrder()));
            this.topK = itemset.stream().limit(k).collect(Collectors.toList());
        }
        return topK;
    }

    public List<ContentItem> getTopK() {
        if(topK == null) {
            itemset.sort(Comparator.comparing(ContentItem::getScore, Comparator.reverseOrder())
                    .thenComparing(ContentItem::getConfidence, Comparator.reverseOrder()));
            this.topK = itemset.stream().limit(DEFAULT_TOPK).collect(Collectors.toList());
        }
        return topK;
    }


    @Override
    public String toString() {
        return "ResultSet{" +
                "contentClass='" + contentClass + '\'' +
                ", itemset=" + itemset +
                '}';
    }
}
