package com.example.ssmstudy.controller;

import com.example.ssmstudy.dto.CreateCommentRequest;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.Comment;
import com.example.ssmstudy.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@PathVariable("articleId") Long articleId,
                                 @Valid @RequestBody CreateCommentRequest request,
                                 @AuthenticationPrincipal UserDetails userDetails){
        return commentService.createComment(articleId, request.getContent(), userDetails.getUsername());
    }

    @GetMapping
    public List<Comment> getArticle(
            @PathVariable(name = "articleId") Long articleId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码不能小于 1");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "每页条数必须在 1 到 100 之间");
        }

        return commentService.getCommentPage(articleId, page, pageSize);
        //return articleService.getArticles(keyword);
    }
}
