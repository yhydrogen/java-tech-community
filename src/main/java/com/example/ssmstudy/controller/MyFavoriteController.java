package com.example.ssmstudy.controller;

import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.service.ArticleFavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/me/favorites")
public class MyFavoriteController {
    private final ArticleFavoriteService articleFavoriteService;

    public MyFavoriteController(ArticleFavoriteService articleFavoriteService) {
        this.articleFavoriteService = articleFavoriteService;
    }

    @GetMapping
    public List<Article> getFavoritePage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码不能小于 1");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "每页条数必须在 1 到 100 之间");
        }

        return articleFavoriteService.getFavoritePage(userDetails.getUsername(), page, pageSize);
    }
}
