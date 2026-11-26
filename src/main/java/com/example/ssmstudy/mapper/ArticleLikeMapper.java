package com.example.ssmstudy.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleLikeMapper {

    @Insert("INSERT INTO article_like (article_id, user_id) VALUES (#{articleId}, #{userId})")
    int insert(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Delete("DELETE FROM article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    int delete(@Param("articleId") Long articleId, @Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM article_like WHERE article_id = #{articleId} AND user_id = #{userId}")
    int count(@Param("articleId") Long articleId, @Param("userId") Long userId);

}
