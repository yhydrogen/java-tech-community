package com.example.ssmstudy.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateArticleRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "正文不能为空")
    private String content;

    public CreateArticleRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
