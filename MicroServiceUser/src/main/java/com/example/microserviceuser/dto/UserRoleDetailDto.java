package com.example.microserviceuser.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRoleDetailDto {
    private Long userId;
    private Long roleId;
    private String roleName;

    @Builder
    public UserRoleDetailDto(Long userId, Long roleId, String roleName) {
        this.userId = userId;
        this.roleId = roleId;
        this.roleName = roleName;
    }
}