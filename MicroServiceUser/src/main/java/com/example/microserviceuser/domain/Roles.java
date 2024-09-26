package com.example.microserviceuser.domain;

import com.example.microserviceuser.enumtype.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Roles extends BaseTime{
    private Long id;    //xml파일에서 자동증가 처리
    private RoleType roleName;

    @Builder
    public Roles(RoleType roleName) {
        this.roleName = roleName;
    }
}
