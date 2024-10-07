package com.example.microserviceuser.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDto {
    private String username;
    private String email;
    private String password;

    //빌더 보류

}
