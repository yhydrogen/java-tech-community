package com.example.ssmstudy.mapper;


import com.example.ssmstudy.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO article_comment (article_id, user_id, content) VALUES (#{articleId}, #{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Select("SELECT id, article_id AS articleId, user_id AS userId, content, created_at AS createdAt FROM article_comment WHERE id = #{id}")
    Comment selectById(@Param("id") Long id);

    @Select("""
            SELECT id, article_id AS articleId, user_id AS userId, content, created_at AS createdAt FROM article_comment
            WHERE article_id = #{articleId}
            ORDER BY created_at DESC, id DESC
            LIMIT #{pageSize} OFFSET #{offset}
            """)
    List<Comment> selectPageByArticleId(
            @Param("articleId") Long articleId,
            @Param("pageSize") int pageSize,
            @Param("offset") long offset);
}
