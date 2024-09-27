package com.example.microserviceuser.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;                // 사용자 ID (필요한 경우)
    private String username;        // 사용자 이름
    private String email;           // 이메일 주소
    private List<String> roles;     // 사용자 권한 목록
}