package com.example.microserviceuser.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Users extends BaseTime{

    private Long id;
    private String username;
    private String email;
    private String password;
}
