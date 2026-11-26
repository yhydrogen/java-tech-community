package com.example.ssmstudy.model;

public class ArticleCacheResult {
    private final boolean hit;
    private final Article article;

    public ArticleCacheResult(boolean hit, Article article) {
        this.hit = hit;
        this.article = article;
    }
    public boolean isHit(){
        return hit;
    }
    public Article getArticle(){
        return article;
    }
}
