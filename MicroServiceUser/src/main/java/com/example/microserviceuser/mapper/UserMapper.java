package com.example.microserviceuser.mapper;


import com.example.microserviceuser.domain.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM Users")
    List<Users> findAllUsers();

    @Select("SELECT * FROM Users WHERE id = #{id}")
    Users findUserById(Long id);

    @Insert("INSERT INTO Users (username, email, password) " +
            "VALUES (#{username}, #{email}, #{password})")
    void insertUser(Users user);


    @Update("UPDATE Users SET username = #{username}, email = #{email}, password = #{password} " +
            "WHERE id = #{id}")
    void updateUser(Users user);

    @Delete("DELETE FROM Users WHERE id = #{id}")
    void deleteUser(Long id);
}
