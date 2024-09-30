package com.example.microserviceuser.mapper;

import com.example.microserviceuser.domain.UserRoles;
import com.example.microserviceuser.domain.Users;
import com.example.microserviceuser.dto.UserRoleDetailDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserRoleMapper {

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

    @Select("SELECT COUNT(*) > 0 " +
        "FROM USERROLES UR "+
        "INNER JOIN USERS U ON UR.USERID = U.id " +
        "INNER JOIN ROLES R ON UR.ROLEID = R.id " +
        "WHERE U.EMAIL = #{email} AND r.roleName = 'ADMIN_AUTH'")
    boolean existAdminRole(@Param("email")String email);

    @Select("SELECT COUNT(*) > 0 " +
            "FROM USERROLES UR "+
            "INNER JOIN USERS U ON UR.USERID = U.id " +
            "INNER JOIN ROLES R ON UR.ROLEID = R.id " +
            "WHERE U.EMAIL = #{email} AND r.roleName = 'TEMP_SUSP_AUTH'")
    boolean existTempSuspendRole(@Param("email")String email);

    @Select("SELECT createdDate "+
            "FROM USERROLES "+
            "WHERE USERID = #{userId} AND roleId = (SELECT id FROM ROLES WHERE roleName = 'TEMP_SUSP_AUTH')")
    LocalDateTime findTimeById(@Param("userId")Long id);

    @Delete("DELETE FROM UserRoles\n" +
            "WHERE userId = #{userId} \n " +
            "AND roleId = (SELECT id FROM Roles WHERE roleName = #{roleName})")
    void deleteUserRoleByUserIdAndRole(@Param("userId") Long userId, @Param("roleName") String roleName);

}