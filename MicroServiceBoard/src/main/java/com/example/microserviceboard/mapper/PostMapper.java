package com.example.microserviceboard.mapper;

import com.example.microserviceboard.domain.Posts;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT * FROM Posts WHERE id = #{id}")
    Posts findById(Long id);

    @Select("SELECT * FROM Posts")
    List<Posts> findAll();

    @Insert("INSERT INTO Posts (title, content, authorId) VALUES (#{title}, #{content}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Posts post);

    @Update("UPDATE Posts SET title = #{title}, content = #{content} WHERE id = #{id}")
    void update(Posts post);

    @Delete("DELETE FROM Posts WHERE id = #{id}")
    void delete(Long id);

}
