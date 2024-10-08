package com.example.microserviceuser.mapper;

import com.example.microserviceuser.domain.Roles;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("SELECT * FROM Roles")
    List<Roles> findAllRoles();

    @Select("SELECT * FROM Roles WHERE id = #{id}")
    Roles findRoleById(Long id);

    @Select("SELECT * FROM Roles WHERE roleName = #{roleName}")
    Roles findByRoleName(String roleName);

    @Insert("INSERT INTO Roles (roleName) " +
            "VALUES (#{roleName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRole(Roles role);

    @Update("UPDATE Roles SET roleName = #{roleName}  WHERE id = #{id}")
    void updateRole(Roles role);

    @Delete("DELETE FROM Roles WHERE id = #{id}")
    void deleteRole(Long id);
}
