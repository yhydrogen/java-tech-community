package com.example.ssmstudy.service;

import com.example.ssmstudy.dto.ArticleContentDiffLine;
import com.example.ssmstudy.dto.ArticleVersionDiffResponse;
import com.example.ssmstudy.mapper.ArticleMapper;
import com.example.ssmstudy.mapper.ArticleVersionMapper;
import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.ArticleCacheResult;
import com.example.ssmstudy.model.ArticleVersion;
import com.example.ssmstudy.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleService {

    //private final AtomicLong nextId = new AtomicLong(4L);
    //private final List<Article> articles = new ArrayList<>();
    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final ArticleCacheService articleCacheService;
    private final ArticleVersionMapper articleVersionMapper;

    public ArticleService(ArticleMapper articleMapper,
                          UserService userService,
                          ArticleCacheService articleCacheService,
                          ArticleVersionMapper articleVersionMapper){
        this.articleMapper = articleMapper;
        this.userService = userService;
        this.articleCacheService = articleCacheService;
        this.articleVersionMapper = articleVersionMapper;
        //articles.add(new Article(1L, "我的第一篇技术文章", "开始学习JAVA后端开发"));
        //articles.add(new Article(2L, "spring MVC入门", "学习处理HTTP请求"));
        //articles.add(new Article(3L, "JAVA集合学习", "练习list and map"));
    }
    public Article createArticle(String title, String content, String username){
        //long id = nextId.getAndIncrement();
        //Article article = new Article(id, title, content);
        //articles.add(article);
        //return article;
        User user = userService.getCurrentUser(username);
        Article article = new Article();
        article.setContent(content);
        article.setTitle(title);
        article.setAuthorId(user.getId());
        article.setVersion(0L);
        articleMapper.insert(article);
        return article;
    }

    public Article getArticleById(Long id){
        if(id==null)return null;
        /*for(Article a:articles)
        {
            if(id.equals(a.getId()))
                return a;
        }*/
        //return null;
        ArticleCacheResult cacheResult = articleCacheService.get(id);
        if(cacheResult.isHit()){
            return cacheResult.getArticle();
        }
        Article article = articleMapper.selectById(id);
        if(article != null){
            articleCacheService.put(article);
        }else {
            articleCacheService.putMissing(id);
        }
        return article;
    }

    public List<Article> getArticles(String keyword){
        if(keyword == null || keyword.isBlank())
            return articleMapper.selectAll();
        /*
        String searchkey = keyword.strip();
        List<Article> result = new LinkedList<>();
        for (Article a: articles){
            if(a.getTitle().contains(searchkey))
                result.add(a);
        }
        return result;*/
        return articleMapper.selectByTitleKeyword(keyword.strip());
    }

    public List<Article> getArticlePage(String keyword, int page, int pageSize){
        Long beforepage = (long) (page-1)*pageSize;
        if (keyword == null || keyword.isBlank()){
            return articleMapper.selectPage(pageSize, beforepage);
        }
        return articleMapper.selectPageByTitleKeyword(keyword.strip(), pageSize, beforepage);
    }

    public List<ArticleVersion> getArticleVersionPage(Long articleId, int page, int pageSize, String username){
        long offset = (long) (page - 1) * pageSize;
        User currentUser = userService.getCurrentUser(username);
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        if (!Objects.equals(article.getAuthorId(), currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "当前用户不是文章的作者");
        }
        return articleVersionMapper.selectPageByArticleId(articleId, pageSize,offset);
    }
    public ArticleVersion getArticleVersion(Long articleId, Long versionId, String username){
        User currentUser = userService.getCurrentUser(username);
        Article article = articleMapper.selectById(articleId);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        if (!Objects.equals(article.getAuthorId(), currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "当前用户不是文章的作者");
        }
        ArticleVersion articleVersion = articleVersionMapper.selectByArticleIdAndVersionId(articleId, versionId);
        if (articleVersion == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "历史版本不存在");
        }
        return articleVersion;
    }
    public ArticleVersionDiffResponse getArticleVersionDiff(
            Long articleId, Long fromVersionId,
            Long toVersionId, String username){
        ArticleVersion fromArticleVersion = getArticleVersion(articleId, fromVersionId, username);
        ArticleVersion toArticleVersion = getArticleVersion(articleId, toVersionId, username);
        ArticleVersionDiffResponse articleVersionDiffResponse = new ArticleVersionDiffResponse();
        articleVersionDiffResponse.setArticleId(articleId);
        articleVersionDiffResponse.setFromTitle(fromArticleVersion.getTitle());
        articleVersionDiffResponse.setToTitle(toArticleVersion.getTitle());
        articleVersionDiffResponse.setFromVersionId(fromVersionId);
        articleVersionDiffResponse.setToVersionId(toVersionId);
        if (!Objects.equals(fromArticleVersion.getTitle(), toArticleVersion.getTitle())){
            articleVersionDiffResponse.setTitleChanged(true);
        }else {
            articleVersionDiffResponse.setTitleChanged(false);
        }
        String[] fromSplit = fromArticleVersion.getContent().split("\\R", -1);
        String[] toSplit = toArticleVersion.getContent().split("\\R", -1);
        List<ArticleContentDiffLine> lines = new ArrayList<>();
        int length = fromSplit.length > toSplit.length ? fromSplit.length : toSplit.length;
        for (int i = 0; i < length; ++i){
            String fromText = i < fromSplit.length ? fromSplit[i] : null;
            String toText = i < toSplit.length ? toSplit[i] : null;
            if (!Objects.equals(fromText, toText)){
                ArticleContentDiffLine line = new ArticleContentDiffLine();
                line.setLineNumber(i + 1);
                line.setFromText(fromText);
                line.setToText(toText);
                lines.add(line);
            }
        }
        articleVersionDiffResponse.setContentChanges(lines);
        return articleVersionDiffResponse;
    }
    @Transactional
    public Article updateArticle(Long id, String title, String content, Long expectedVersion, String username){
        User currentUser = userService.getCurrentUser(username);
        Article article = articleMapper.selectById(id);
        if(article == null){
            return null;
        }
        if(!Objects.equals(article.getAuthorId(), currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只能编辑自己的文章");
        }
        if (expectedVersion == null || expectedVersion < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "文章版本不符合规则");
        }
        if (!Objects.equals(expectedVersion, article.getVersion())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "文章已发生变化，请刷新后重试");
        }
        ArticleVersion articleVersion = new ArticleVersion();
        articleVersion.setArticleId(article.getId());
        articleVersion.setContent(article.getContent());
        articleVersion.setTitle(article.getTitle());
        articleVersion.setEditorId(currentUser.getId());
        int insert = articleVersionMapper.insert(articleVersion);
        if(insert != 1){
            throw new IllegalStateException("保存文章快照失败");
        }
        article.setTitle(title);
        article.setContent(content);
        int rows = articleMapper.updateById(article);
        if(rows == 1){
            article.setVersion(expectedVersion + 1);
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            //TransactionSynchronization.super.afterCommit();
                            articleCacheService.evict(article.getId());
                        }
                    }
            );
            return article;
        }
        else if(rows == 0){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "文章状态已变化，请刷新后重试");
        }
        else {
            throw new IllegalStateException("更新文章失败");
        }
    }
    @Transactional
    public Article restoreArticleVersion(Long articleId, Long versionId, Long expectedVersion, String username){
        ArticleVersion articleVersion = getArticleVersion(articleId, versionId, username);
        Article article = updateArticle(articleId, articleVersion.getTitle(), articleVersion.getContent(), expectedVersion, username);
        if(article == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章不存在");
        }
        return article;
    }

    @Transactional
    public boolean deleteArticle(Long id, String username){
        User currentUser = userService.getCurrentUser(username);
        Article article = articleMapper.selectById(id);
        if(article == null){
            return false;
        }
        if(!Objects.equals(article.getAuthorId(), currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只能删除自己的文章");
        }
        int rows = articleMapper.deleteById(id, currentUser.getId());
        if(rows == 1){
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            articleCacheService.evict(article.getId());
                        }
                    }
            );
            return true;
        }
        else
            return false;
    }
}
