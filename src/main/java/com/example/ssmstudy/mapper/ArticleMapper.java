package com.example.ssmstudy.mapper;


import com.example.ssmstudy.model.Article;
import com.example.ssmstudy.model.ArticleLikeCount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("SELECT id, title, content, author_id AS authorId, version FROM article WHERE id = #{id}")
    Article selectById(@Param("id") Long id);
    @Select("SELECT id, title, content, author_id AS authorId, version FROM article ORDER BY id ASC")
    List<Article> selectAll();
    @Select("SELECT id, title, content, author_id AS authorId, version FROM article ORDER BY created_at DESC, id DESC LIMIT #{pageSize} OFFSET #{offset}")
    List<Article> selectPage(@Param("pageSize") Integer pageSize, @Param("offset") Long offset);
    @Select("SELECT id, title, content, author_id AS authorId, version FROM article WHERE title LIKE CONCAT('%', #{keyword}, '%') ORDER BY id ASC")
    List<Article> selectByTitleKeyword(@Param("keyword") String keyword);
    @Select("""
            SELECT id, title, content, author_id AS authorId, version FROM article
            WHERE title LIKE CONCAT('%', #{keyword}, '%')
            ORDER BY created_at DESC, id DESC
            LIMIT #{pageSize} OFFSET #{offset}
            """)
    List<Article> selectPageByTitleKeyword(@Param("keyword") String keyword,
                                           @Param("pageSize") Integer pageSize,
                                           @Param("offset") Long offset);
    @Select("""
    SELECT id AS articleId, like_count AS likeCount
    FROM article
    WHERE like_count > 0
    """)
    List<ArticleLikeCount> selectArticleLikeCounts();

    @Insert("INSERT INTO article (title, content, author_id) VALUES (#{title}, #{content}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Update("""
    UPDATE article SET title = #{title}, content = #{content}, version = version + 1
                   WHERE id = #{id} AND author_id = #{authorId} AND version = #{version};
    """)
    int updateById(Article article);
    @Update("UPDATE article SET like_count = like_count + 1 WHERE id = #{articleId}")
    int incrementLikeCount(@Param("articleId") Long articleId);
    @Update("UPDATE article SET like_count = like_count - 1 WHERE id = #{articleId} AND like_count > 0")
    int decrementLikeCount(@Param("articleId") Long articleId);

    @Delete("DELETE FROM article WHERE id = #{id} AND author_id = #{authorId};")
    int deleteById(@Param("id") Long id, @Param("authorId") Long authorId);
}
