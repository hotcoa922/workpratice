package com.example.microserviceuser.service;

import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;

public interface UserService {

    //회원가입
    void registerUser(RegisterDto registerDto);

    //로그인
    String login(LoginDto loginDto);

    //로그아웃
    void logout();

    //임시정지
    void tempSuspendRole(Long userId);

    //영구정지
    void permSuspendRole(Long userId);

    //관리자부여
    void grantAdminRole(Long userId);
}
