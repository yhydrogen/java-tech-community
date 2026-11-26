package com.example.ssmstudy.service;

import com.example.ssmstudy.mapper.ArticleMapper;
import com.example.ssmstudy.model.ArticleLikeCount;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ArticleHotService {
    private static final String HOT_KEY = "community:article:hot";
    private static final Logger log = LoggerFactory.getLogger(ArticleHotService.class);

    private final ArticleMapper articleMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public ArticleHotService(ArticleMapper articleMapper, StringRedisTemplate stringRedisTemplate) {
        this.articleMapper = articleMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Scheduled(fixedDelay = 60_000, initialDelay = 5_000)
    public void rebuild() {
        List<ArticleLikeCount> articleLikeCounts = articleMapper.selectArticleLikeCounts();
        if (articleLikeCounts.isEmpty()) {
            stringRedisTemplate.delete(HOT_KEY);
            return;
        }

        String tempKey = HOT_KEY + ":tmp:" + UUID.randomUUID();
        try {
            for (ArticleLikeCount articleLikeCount : articleLikeCounts) {
                stringRedisTemplate.opsForZSet().add(
                        tempKey,
                        articleLikeCount.getArticleId().toString(),
                        articleLikeCount.getLikeCount().doubleValue());
            }
            // 只有全部写入成功，才用完整的新榜替换正式榜单。
            stringRedisTemplate.rename(tempKey, HOT_KEY);
        } finally {
            try {
                // rename 成功后临时 Key 已不存在，重复删除可正常结束。
                stringRedisTemplate.delete(tempKey);
            } catch (DataAccessException e) {
                log.warn("清理热门榜临时 Key 失败，tempKey={}", tempKey, e);
            }
        }
    }

    public List<ArticleLikeCount> getTopArticles(int limit){
        if(limit < 1 || limit > 100){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit不在1-100范围内");
        }
        Set<ZSetOperations.@NonNull TypedTuple<String>> typedTuples;
        try {
            typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(
                    HOT_KEY, 0, limit - 1);
        }catch (DataAccessException e){
            log.warn("热门榜暂时不可用，limit={}", limit, e);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "热门榜暂时不可用", e);
        }
        ArrayList<ArticleLikeCount> articleLikeCounts = new ArrayList<>();

        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples){
            ArticleLikeCount articleLikeCount = new ArticleLikeCount();
            articleLikeCount.setArticleId(Long.valueOf(typedTuple.getValue()));
            articleLikeCount.setLikeCount(typedTuple.getScore().longValue());
            articleLikeCounts.add(articleLikeCount);
        }
        return articleLikeCounts;
    }
}
