package com.example.microserviceuser.service;

import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;
import com.example.microserviceuser.dto.UserDto;

public interface UserService {


    UserDto getUserByUsername(String username);

    //회원가입
    void registerUser(RegisterDto registerDto);

    //로그인
    String login(LoginDto loginDto);

    //어드민 권환 획득
    void tryAdminAuth(String secretCode);


//    //로그아웃
//    void logout();

    //임시정지
    void tempSuspendRole(Long userId);

    //영구정지
    void permSuspendRole(Long userId);

}
