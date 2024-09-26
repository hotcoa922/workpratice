package com.example.microserviceuser.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Users extends BaseTime{

    private Long id;    //xml파일에서 자동증가 처리
    private String username;
    private String email;
    private String password;

    @Builder
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


}
