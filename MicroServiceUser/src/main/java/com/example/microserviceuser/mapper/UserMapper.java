package com.example.microserviceuser.mapper;


import com.example.microserviceuser.domain.Users;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM Users")
    List<Users> findAllUsers();

    @Select("SELECT * FROM Users WHERE id = #{id}")
    Users findUserById(@Param("id") Long id);

    @Select("SELECT * FROM Users WHERE username = #{username}")
    Users findUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM Users WHERE email = #{email}")
    Users findUserByEmail(@Param("email")String email);

    @Insert("INSERT INTO Users (username, email, password) " +
            "VALUES (#{username}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(Users user);


    @Update("UPDATE Users SET username = #{username}, email = #{email}, password = #{password} " +
            "WHERE id = #{id}")
    void updateUser(Users user);

    @Delete("DELETE FROM Users WHERE id = #{id}")
    void deleteUser(Long id);

    @Select("SELECT u.* " +
            "FROM Users u " +
            "INNER JOIN UserRoles ur ON u.id = ur.userId " +
            "INNER JOIN Roles r ON ur.roleId = r.id "+
            "WHERE r.roleName = 'TEMP_SUSP_AUTH'")
    List<Users> findUsersWithTempSuspendRole( );
}
