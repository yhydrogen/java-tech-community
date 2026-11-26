package com.example.ssmstudy.service;

import com.example.ssmstudy.mapper.ArticleFavoriteMapper;
import com.example.ssmstudy.mapper.ArticleLikeMapper;
import com.example.ssmstudy.mapper.ArticleMapper;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ArticleFavoriteService {
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final ArticleFavoriteMapper articleFavoriteMapper;

    public ArticleFavoriteService(ArticleMapper articleMapper, UserService userService, ArticleFavoriteMapper articleFavoriteMapper) {
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.articleFavoriteMapper = articleFavoriteMapper;
    }

    public void favorite(Long articleId, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User user = userService.getCurrentUser(username);
        try{
            articleFavoriteMapper.insert(articleId, user.getId());
        }catch (DuplicateKeyException ex){
            return;
        }
    }
    public void unfavorite(Long articleId, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User user = userService.getCurrentUser(username);
        articleFavoriteMapper.delete(articleId, user.getId());
    }
    public List<Article> getFavoritePage(String username, int page, int pageSize){
        User user = userService.getCurrentUser(username);
        long offset = (long) (page - 1) * pageSize;
        return articleFavoriteMapper.selectPageByUserId(user.getId(), pageSize, offset);
    }
}
