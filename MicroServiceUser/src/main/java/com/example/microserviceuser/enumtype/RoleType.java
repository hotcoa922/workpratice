package com.example.microserviceuser.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER_AUTH("일반 사용자"),
    ADMIN_AUTH("관리자"),
    TEMP_SUSP_AUTH("임시 정지"),
    PERM_SUSP_AUTH("영구 정지");


    private final String description;

}
