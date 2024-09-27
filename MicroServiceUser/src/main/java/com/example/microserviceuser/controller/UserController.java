package com.example.microserviceuser.controller;


import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;
import com.example.microserviceuser.dto.UserDto;
import com.example.microserviceuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        userService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}