package com.example.microserviceuser.controller;


import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;
import com.example.microserviceuser.dto.UserDto;
import com.example.microserviceuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // 사용자 정보를 데이터베이스에서 가져옵니다.
            UserDto userDto = userService.getUserByUsername(username);
            return userDto;
        } else {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterDto registerDto) {
        userService.registerUser(registerDto);
        return new ResponseEntity<>(HttpStatus.CREATED);  // 회원가입 성공 시 201 Created 응답
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        return ResponseEntity.ok(token);  // 로그인 성공 시 JWT 토큰 반환
    }

    //관리자 권한 받기
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/secret")
    public ResponseEntity<String> tryAdmin(@RequestBody Map<String, String> secretCodeMap){ //JSON 형식으로 전달된 데이터를 올바르게 처리하기 위함
        try {
            String secretCode = secretCodeMap.get("secretCode");
            userService.tryAdminAuth(secretCode);
            return ResponseEntity.ok("관리자 권한이 부여되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tempben")
    public ResponseEntity<String> giveTempBen(@RequestBody Long targetId){
        try{
            userService.tempSuspendRole(targetId);
            return ResponseEntity.ok("대상자 임시정지 완료");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/permben")
    public ResponseEntity<String> givePermBen(@RequestBody Long targetId){
        try{
            userService.permSuspendRole(targetId);
            return ResponseEntity.ok("대상자 영구정지 완료");
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





    // 로그아웃
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout() {
//        userService.logout();
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


}