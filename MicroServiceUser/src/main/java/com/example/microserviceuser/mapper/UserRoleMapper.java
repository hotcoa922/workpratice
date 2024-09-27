package com.example.microserviceuser.mapper;

import com.example.microserviceuser.domain.UserRoles;
import com.example.microserviceuser.domain.Users;
import com.example.microserviceuser.dto.UserRoleDetailDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM UserRoles")
    List<UserRoles> findAllUserRoles(); //모든 사용자 역할(UserRoles) 데이터를 조회


    @Select("SELECT ur.userId, ur.roleId, r.roleName " +
            "FROM UserRoles ur " +
            "INNER JOIN Users u ON ur.userId = u.id " +
            "INNER JOIN Roles r ON ur.roleId = r.id " +
            "WHERE u.email = #{email}")
    List<UserRoleDetailDto> findUserRoleByEmail(@Param("email") String email);

    @Select("SELECT ur.id AS id, ur.userId AS userId, ur.roleId AS roleId, r.roleName AS roleName " +
            "FROM UserRoles ur " +
            "INNER JOIN Roles r ON ur.roleId = r.id " +
            "WHERE ur.userId = #{userId}")
    List<UserRoleDetailDto> findUserRolesByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM Users WHERE username = #{username}")
    Users findUserByUsername(@Param("username") String username);

    @Insert("INSERT INTO UserRoles (userId, roleId) " +
            "VALUES (#{userId}, #{roleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUserRole(UserRoles userRole);

    @Update("UPDATE UserRoles SET userId = #{userId}, roleId = #{roleId} " +
            "WHERE id = #{id}")
    void updateUserRole(UserRoles userRole);

    @Delete("DELETE FROM UserRoles WHERE id = #{id}")
    void deleteUserRole(Long id);
}