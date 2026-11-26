package com.example.ssmstudy.mapper;

import com.example.ssmstudy.model.ArticleVersion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleVersionMapper {
    @Insert("""
    INSERT INTO article_version (article_id, title, content, editor_id)
    VALUES (#{articleId}, #{title}, #{content}, #{editorId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ArticleVersion articleVersion);

    @Select("""
            SELECT id, article_id AS articleId, title, content, editor_id AS editorId, created_at AS createdAt
            FROM article_version
            WHERE article_id = #{articleId}
            ORDER BY id DESC
            LIMIT #{pageSize} OFFSET #{offset}
            """)
    List<ArticleVersion> selectPageByArticleId(
            @Param("articleId") Long articleId,
            @Param("pageSize") int pageSize,
            @Param("offset") long offset);

    @Select("""
            SELECT id, article_id AS articleId, title, content, editor_id AS editorId, created_at AS createdAt
            FROM article_version
            WHERE article_id = #{articleId} AND id = #{versionId}
            """)
    ArticleVersion selectByArticleIdAndVersionId(
            @Param("articleId") Long articleId,
            @Param("versionId") Long versionId);
}
