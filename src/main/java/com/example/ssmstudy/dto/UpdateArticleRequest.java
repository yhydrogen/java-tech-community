package com.example.ssmstudy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class UpdateArticleRequest {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "正文不能为空")
    private String content;
    @NotNull(message = "期望版本号不能为空")
    @PositiveOrZero(message = "期望版本号不能小于0")
    private Long expectedVersion;

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

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public void setExpectedVersion(Long expectedVersion) {
        this.expectedVersion = expectedVersion;
    }
}
