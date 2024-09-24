package com.example.microserviceuser.mapper;

import com.example.microserviceuser.domain.UserRoles;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM UserRoles")
    List<UserRoles> findAllUserRoles();

    @Select("SELECT * FROM UserRoles WHERE id = #{id}")
    UserRoles findUserRoleById(Long id);

    @Insert("INSERT INTO UserRoles (userId, roleId) " +
            "VALUES (#{userId}, #{roleId})")
    void insertUserRole(UserRoles userRole);

    @Update("UPDATE UserRoles SET userId = #{userId}, roleId = #{roleId} " +
            "WHERE id = #{id}")
    void updateUserRole(UserRoles userRole);

    @Delete("DELETE FROM UserRoles WHERE id = #{id}")
    void deleteUserRole(Long id);
}