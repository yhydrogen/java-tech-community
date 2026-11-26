package com.example.ssmstudy.service;

import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.ArticleCacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Service
public class ArticleCacheService {

    private final StringRedisTemplate redisTemplate;
    private final JsonMapper jsonMapper;
    private static final String NULL_MARKER = "__NULL__";
    private static final Logger log = LoggerFactory.getLogger(ArticleCacheService.class);

    public ArticleCacheService(StringRedisTemplate redisTemplate, JsonMapper jsonMapper) {
        this.redisTemplate = redisTemplate;
        this.jsonMapper = jsonMapper;
    }

    public ArticleCacheResult get(Long articleId){
        String key = "community:article:detail:" + articleId;
        String json;
        try {
            json = redisTemplate.opsForValue().get(key);
        } catch (DataAccessException e) {
            log.warn("读取文章缓存失败，articleId={}", articleId, e);
            return new ArticleCacheResult(false, null);
        }
        if(json == null){
            return new ArticleCacheResult(false, null);
        }else if(NULL_MARKER.equals(json)){
            return new ArticleCacheResult(true, null);
        }
        Article article = jsonMapper.readValue(json, Article.class);
        if(article == null || article.getVersion() == null){
            return new ArticleCacheResult(false, null);
        }
        ArticleCacheResult cacheResult = new ArticleCacheResult(true, article);
        return cacheResult;
    }

    public void put(Article article){
        String key = "community:article:detail:" + article.getId();
        String json = jsonMapper.writeValueAsString(article);
        try {
            redisTemplate.opsForValue().set(key, json, Duration.ofMinutes(5));
        } catch (DataAccessException e) {
            log.warn("回填文章缓存失败，articleId={}", article.getId(), e);
        }
    }

    public void putMissing(Long articleId){
        String key = "community:article:detail:" + articleId;
        try {
            redisTemplate.opsForValue().set(
                    key, NULL_MARKER, Duration.ofSeconds(30));
        } catch (DataAccessException e) {
            log.warn("回填文章不存在标记失败，articleId={}", articleId, e);
        }
    }

    public void evict(Long articleId){
        String key = "community:article:detail:" + articleId;
        try {
            redisTemplate.delete(key);
        } catch (DataAccessException e) {
            log.warn("删除文章缓存失败，articleId={}", articleId, e);
        }
    }
}
