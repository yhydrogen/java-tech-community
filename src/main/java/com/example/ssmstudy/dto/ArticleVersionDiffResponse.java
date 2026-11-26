package com.example.ssmstudy.dto;

import java.util.List;

public class ArticleVersionDiffResponse {
    private Long articleId;
    private Long fromVersionId;
    private Long toVersionId;
    private String fromTitle;
    private String toTitle;
    private boolean titleChanged;
    private List<ArticleContentDiffLine> contentChanges;

    public ArticleVersionDiffResponse() {
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getFromVersionId() {
        return fromVersionId;
    }

    public void setFromVersionId(Long fromVersionId) {
        this.fromVersionId = fromVersionId;
    }

    public Long getToVersionId() {
        return toVersionId;
    }

    public void setToVersionId(Long toVersionId) {
        this.toVersionId = toVersionId;
    }

    public boolean isTitleChanged() {
        return titleChanged;
    }

    public String getFromTitle() {
        return fromTitle;
    }

    public void setFromTitle(String fromTitle) {
        this.fromTitle = fromTitle;
    }

    public String getToTitle() {
        return toTitle;
    }

    public void setToTitle(String toTitle) {
        this.toTitle = toTitle;
    }

    public void setTitleChanged(boolean titleChanged) {
        this.titleChanged = titleChanged;
    }

    public List<ArticleContentDiffLine> getContentChanges() {
        return contentChanges;
    }

    public void setContentChanges(List<ArticleContentDiffLine> contentChanges) {
        this.contentChanges = contentChanges;
    }
}
