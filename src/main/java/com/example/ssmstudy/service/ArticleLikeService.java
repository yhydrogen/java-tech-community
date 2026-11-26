package com.example.ssmstudy.service;


import com.example.ssmstudy.mapper.ArticleLikeMapper;
import com.example.ssmstudy.mapper.ArticleMapper;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArticleLikeService {
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final ArticleLikeMapper articleLikeMapper;

    public ArticleLikeService(ArticleMapper articleMapper, UserService userService, ArticleLikeMapper articleLikeMapper) {
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.articleLikeMapper = articleLikeMapper;
    }

    @Transactional
    public void like(Long articleId, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User user = userService.getCurrentUser(username);
        try {
            articleLikeMapper.insert(articleId, user.getId());
        } catch (DuplicateKeyException ex) {
            return;
        }
        if (articleMapper.incrementLikeCount(articleId) != 1) {
            throw new IllegalStateException("点赞计数增加失败");
        }
    }

    @Transactional
    public void unlike(Long articleId, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User user = userService.getCurrentUser(username);
        int deletedRows = articleLikeMapper.delete(articleId, user.getId());
        if (deletedRows == 0) {
            return;
        }
        if (articleMapper.decrementLikeCount(articleId) != 1) {
            throw new IllegalStateException("点赞计数减少失败");
        }
    }

    public boolean hasLiked(Long articleId, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User user = userService.getCurrentUser(username);
        return articleLikeMapper.count(articleId, user.getId()) == 1 ? true : false;
    }
}
