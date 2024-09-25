package com.example.microserviceuser.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoles extends BaseTime{
    private Long id;
    private Long userId;
    private Long roleId;

    @Builder
    public UserRoles(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
