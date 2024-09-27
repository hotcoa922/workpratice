package com.example.microserviceboard.mapper;

import com.example.microserviceboard.domain.Comments;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM Comments")
    List<Comments> findAllComments();

    @Select("SELECT * FROM Comments WHERE id = #{id}")
    Comments findCommentById(Long id);

    @Insert("INSERT INTO Comments (postId, content, authorEmail) VALUES(#{postId}, #{content}, #{authorEmail})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createComment(Comments comment);

    @Update("UPDATE Comments SET content = #{content} WHERE id = #{id}")
    int updateComment(Comments comment);

    @Delete("DELETE FROM Comments WHERE id = #{id}")
    int deleteComment(Long id);
}
