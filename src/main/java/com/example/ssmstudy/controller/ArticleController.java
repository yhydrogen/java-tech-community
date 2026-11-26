package com.example.ssmstudy.controller;


import com.example.ssmstudy.dto.ArticleVersionDiffResponse;
import com.example.ssmstudy.dto.CreateArticleRequest;
import com.example.ssmstudy.dto.RestoreArticleVersionRequest;
import com.example.ssmstudy.dto.UpdateArticleRequest;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.ArticleLikeCount;
import com.example.ssmstudy.model.ArticleVersion;
import com.example.ssmstudy.service.ArticleHotService;
import com.example.ssmstudy.service.ArticleService;
import jakarta.validation.Valid;
import likou.ListNode;
import org.apache.ibatis.annotations.Update;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleHotService articleHotService;
    private final ArticleService articleService;

    public ArticleController(ArticleHotService articleHotService, ArticleService articleService) {
        this.articleHotService = articleHotService;
        this.articleService = articleService;
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable("id") Long id){
        Article article = articleService.getArticleById(id);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        return article;
    }
    @GetMapping
    public List<Article> getArticle(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码不能小于 1");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "每页条数必须在 1 到 100 之间");
        }

        return articleService.getArticlePage(keyword, page, pageSize);
        //return articleService.getArticles(keyword);
    }
    @GetMapping("/{articleId}/versions")
    public List<ArticleVersion> getArticleVersionPage(
            @PathVariable("articleId") Long articleId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @AuthenticationPrincipal UserDetails userDetails){
        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "页码不能小于 1");
        }
        if (pageSize < 1 || pageSize > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "每页条数必须在 1 到 100 之间");
        }
        return articleService.getArticleVersionPage(articleId, page, pageSize, userDetails.getUsername());
    }
    @GetMapping("/{articleId}/versions/{versionId}")
    public ArticleVersion getArticleVersion(
            @PathVariable("articleId") Long articleId,
            @PathVariable("versionId") Long versionId,
            @AuthenticationPrincipal UserDetails userDetails){
        return articleService.getArticleVersion(articleId, versionId, userDetails.getUsername());
    }
    @GetMapping("/{articleId}/versions/diff")
    public ArticleVersionDiffResponse getArticleVersionDiff(
            @PathVariable("articleId") Long articleId,
            @RequestParam("fromVersionId") Long fromVersionId,
            @RequestParam("toVersionId") Long toVersionId,
            @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        ArticleVersionDiffResponse articleVersionDiff = articleService.getArticleVersionDiff(
                articleId, fromVersionId, toVersionId, username);
        return articleVersionDiff;
    }

    @PostMapping("/{articleId}/versions/{versionId}/restore")
    public Article restoreArticleVersion(
            @PathVariable("articleId") Long articleId,
            @PathVariable("versionId") Long versionId,
            @Valid @RequestBody RestoreArticleVersionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return articleService.restoreArticleVersion(
                articleId, versionId, request.getExpectedVersion(), userDetails.getUsername());
    }

    @GetMapping("/hot")
    public List<ArticleLikeCount> selectArticleLikeCount(@RequestParam(name = "limit", defaultValue = "10") int limit){
        return articleHotService.getTopArticles(limit);
    }

    @PutMapping("/{id}")
    public Article updateArticleById(
            @PathVariable("id")Long id,
            @Valid @RequestBody UpdateArticleRequest request,
            @AuthenticationPrincipal UserDetails userDetails){
        /*if(request.getTitle() == null || request.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "标题不能为空");
        if(request.getContent() == null || request.getContent().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "正文不能为空");*/
        Article article = articleService.updateArticle(id, request.getTitle(),
                request.getContent(), request.getExpectedVersion(), userDetails.getUsername());
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        return article;
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticleById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails){
        if(!articleService.deleteArticle(id, userDetails.getUsername())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"文章不存在");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Article createArticle(@Valid @RequestBody CreateArticleRequest request,
                                 @AuthenticationPrincipal UserDetails userDetails){
        /*if(request.getTitle() == null || request.getTitle().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "标题不能为空");
        if(request.getContent() == null || request.getContent().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "正文不能为空");*/

        return articleService.createArticle(request.getTitle(), request.getContent(), userDetails.getUsername());
    }

}
