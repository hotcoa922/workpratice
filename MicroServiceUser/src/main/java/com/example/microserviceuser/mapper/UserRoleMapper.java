package com.example.microserviceuser.mapper;

import com.example.microserviceuser.domain.UserRoles;
import com.example.microserviceuser.dto.UserRoleDetailDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRoleMapper {

    @Select("SELECT * FROM UserRoles")
    List<UserRoles> findAllUserRoles(); //모든 사용자 역할(UserRoles) 데이터를 조회

    @Select("    SELECT ur.userId, ur.roleId, r.roleName" +
            "    FROM UserRoles ur\n" +
            "    INNER JOIN Users u ON ur.userId = u.id" +
            "    INNER JOIN Roles r ON ur.roleId = r.id" +
            "    WHERE u.email = #{email}") //특정 사용자의 이메일을 기준으로 그 사용자와 연결된 역할을 조회
    List<UserRoleDetailDto> findUserRoleByEmail(String email);

    @Insert("INSERT INTO UserRoles (userId, roleId) " +
            "VALUES (#{userId}, #{roleId})")
    void insertUserRole(UserRoles userRole);

    @Update("UPDATE UserRoles SET userId = #{userId}, roleId = #{roleId} " +
            "WHERE id = #{id}")
    void updateUserRole(UserRoles userRole);

    @Delete("DELETE FROM UserRoles WHERE id = #{id}")
    void deleteUserRole(Long id);
}