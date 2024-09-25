package com.example.microserviceuser.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class LoginDto {
    private String email;
    private String password;
}
