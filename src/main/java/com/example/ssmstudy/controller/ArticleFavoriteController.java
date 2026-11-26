package com.example.ssmstudy.controller;

import com.example.ssmstudy.service.ArticleFavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/{articleId}/favorite")
public class ArticleFavoriteController {
    private final ArticleFavoriteService articleFavoriteService;

    public ArticleFavoriteController(ArticleFavoriteService articleFavoriteService) {
        this.articleFavoriteService = articleFavoriteService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favorite(@PathVariable("articleId")Long articleId, @AuthenticationPrincipal UserDetails userDetails) {
        articleFavoriteService.favorite(articleId, userDetails.getUsername());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlike(@PathVariable("articleId") Long articleId,
                       @AuthenticationPrincipal UserDetails userDetails) {
        articleFavoriteService.unfavorite(articleId, userDetails.getUsername());
    }
}
