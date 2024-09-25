package com.example.microserviceuser.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Roles extends BaseTime{
    private Long id;
    private String roleName;

    @Builder
    public Roles(String roleName) {
        this.roleName = roleName;
    }
}
