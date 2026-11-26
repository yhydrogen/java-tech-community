package com.example.ssmstudy.controller;

import com.example.ssmstudy.service.ArticleLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles/{articleId}/like")
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    public ArticleLikeController(ArticleLikeService articleLikeService) {
        this.articleLikeService = articleLikeService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void like(@PathVariable("articleId") Long articleId,
                     @AuthenticationPrincipal UserDetails userDetails) {
        articleLikeService.like(articleId, userDetails.getUsername());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable("articleId") Long articleId,
                       @AuthenticationPrincipal UserDetails userDetails) {
        articleLikeService.unlike(articleId, userDetails.getUsername());
    }

    @GetMapping
    public boolean hasLiked(@PathVariable("articleId") Long articleId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        return articleLikeService.hasLiked(articleId, userDetails.getUsername());
    }
}
