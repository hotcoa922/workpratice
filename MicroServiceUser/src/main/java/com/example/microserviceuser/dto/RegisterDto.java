package com.example.microserviceuser.dto;


import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;

    //빌더 보류

}
