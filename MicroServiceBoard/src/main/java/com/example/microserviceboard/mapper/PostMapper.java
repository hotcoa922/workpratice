package com.example.microserviceboard.mapper;

import com.example.microserviceboard.domain.Posts;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {


    @Select("SELECT * FROM Posts WHERE id = #{id}")
    Posts findPostById(Long id);

    @Select("SELECT * FROM Posts")
    List<Posts> findAllPosts();

    @Insert("INSERT INTO Posts (title, content, authorEmail) VALUES (#{title}, #{content}, #{authorEmail})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createPost(Posts post);

    @Update("UPDATE Posts SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updatePost(Posts post);

    @Delete("DELETE FROM Posts WHERE id = #{id}")
    void deletePost(Long id);

}
