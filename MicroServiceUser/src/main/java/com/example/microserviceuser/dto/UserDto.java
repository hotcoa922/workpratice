package com.example.microserviceuser.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class UserDto {
    private Long id;                // 사용자 ID (필요한 경우)
    private String username;        // 사용자 이름
    private String email;           // 이메일 주소
    private List<String> roles;     // 사용자 권한 목록

    @Builder
    public UserDto(Long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}