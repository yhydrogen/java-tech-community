package com.example.ssmstudy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class RestoreArticleVersionRequest {
    @NotNull(message = "期望版本号不能为空")
    @PositiveOrZero(message = "期望版本号不能小于0")
    private Long expectedVersion;

    public Long getExpectedVersion() {
        return expectedVersion;
    }

    public void setExpectedVersion(Long expectedVersion) {
        this.expectedVersion = expectedVersion;
    }
}
