package com.example.ssmstudy.mapper;

import com.example.ssmstudy.model.Article;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface ArticleFavoriteMapper {

    @Insert("INSERT INTO article_favorite (article_id, user_id) VALUES (#{articleId}, #{userId})")
    int insert(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Delete("DELETE FROM article_favorite WHERE article_id = #{articleId} AND user_id = #{userId}")
    int delete(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Select("""
            SELECT a.id, a.title, a.content, a.author_id AS authorId, a.version
            FROM article_favorite f JOIN article a ON a.id = f.article_id
            WHERE f.user_id = #{userId}
            ORDER BY f.created_at DESC, f.article_id DESC
            LIMIT #{pageSize} OFFSET #{offset}
            """)
    List<Article> selectPageByUserId(
            @Param("userId") Long userId,
            @Param("pageSize") int pageSize,
            @Param("offset") long offset);
}
