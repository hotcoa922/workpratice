package com.example.microserviceuser.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRoles extends BaseTime{
    private Long userId;
    private Long roleId;
}
