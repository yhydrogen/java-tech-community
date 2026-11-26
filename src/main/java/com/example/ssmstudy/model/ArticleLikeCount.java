package com.example.ssmstudy.model;

public class ArticleLikeCount {
    private Long articleId;
    private Long likeCount;

    public ArticleLikeCount() {
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
