package com.example.microserviceboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
//    private String username;
    private String email;
    private List<String> roles; // 사용자 권한 정보

    @Builder
    public UserDto(String email, List<String> roles) {
//        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
