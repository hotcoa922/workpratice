package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String email;
    private List<String> roles; // 사용자 권한 정보

    @Builder
    public UserDto(String username, String email, List<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
