package com.example.ssmstudy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class CommentRateLimitService {

    private static final Logger log = LoggerFactory.getLogger(CommentRateLimitService.class);
    private static final String PRE_KEY = "community:rate-limit:comment:user:";
    private static final long WINDOW_SECONDS = 60L;
    private static final long MAX_ATTEMPTS = 5L;
    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> script;

    public CommentRateLimitService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.script = new DefaultRedisScript<>();
        this.script.setLocation(new ClassPathResource("scripts/comment-rate-limit.lua"));
        this.script.setResultType(Long.class);
    }

    public void checkAllowed(Long userId){
        String key = PRE_KEY + userId;
        Long count;
        try {
            count = stringRedisTemplate.execute(script, List.of(key), String.valueOf(WINDOW_SECONDS));
        }catch (DataAccessException e){
            log.warn("评论限流调用失败，userId={}", userId, e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "评论限流暂时不可用，请稍后重试", e);
        }
        if (count == null || count < 1) {
            log.warn("评论限流返回异常计数，userId={}, count={}", userId, count);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "评论限流暂时不可用，请稍后重试");
        }

        if (count > MAX_ATTEMPTS) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "评论过于频繁，请稍后重试");
        }
    }

}
