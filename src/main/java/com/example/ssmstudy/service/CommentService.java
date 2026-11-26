package com.example.ssmstudy.service;

import com.example.ssmstudy.mapper.ArticleMapper;
import com.example.ssmstudy.mapper.CommentMapper;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.Comment;
import com.example.ssmstudy.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final CommentMapper commentMapper;
    private final CommentRateLimitService commentRateLimitService;

    public CommentService(ArticleMapper articleMapper, UserService userService, CommentMapper commentMapper,CommentRateLimitService commentRateLimitService) {
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.commentMapper = commentMapper;
        this.commentRateLimitService = commentRateLimitService;
    }

    public Comment createComment(Long articleId, String content, String username){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        User currentUser = userService.getCurrentUser(username);
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setUserId(currentUser.getId());
        commentRateLimitService.checkAllowed(currentUser.getId());
        int row = commentMapper.insert(comment);
        if(row != 1){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "评论发布失败");
        }
        Comment currentComment = commentMapper.selectById(comment.getId());
        return currentComment;
    }

    public List<Comment> getCommentPage(Long articleId, int page, int pageSize){
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        long offset = (long) (page - 1) * pageSize;
        return commentMapper.selectPageByArticleId(articleId, pageSize, offset);
    }
}
