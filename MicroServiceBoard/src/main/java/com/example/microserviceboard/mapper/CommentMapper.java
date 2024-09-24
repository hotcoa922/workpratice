package com.example.microserviceboard.mapper;

import com.example.microserviceboard.domain.Comments;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("SELECT * FROM Comments")
    List<Comments> findAll();

    @Select("SELECT * FROM Comments WHERE id = #{id}")
    Comments findById(Long id);

    @Insert("INSERT INTO Comments (postId, content, authorId) VALUES(#{postId}, #{content}, #{authorId})")
    int insert(Comments comment);

    @Update("UPDATE Comments SET content = #{content}, authorId = #{authorId} WHERE id = #{id}")
    int update(Comments comment);

    @Delete("DELETE FROM Comments WHERE id = #{id}")
    int delete(Long id);
}
