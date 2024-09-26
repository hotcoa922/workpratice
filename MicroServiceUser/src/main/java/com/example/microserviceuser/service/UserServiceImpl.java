package com.example.microserviceuser.service;

import com.example.microserviceuser.domain.Roles;
import com.example.microserviceuser.domain.UserRoles;
import com.example.microserviceuser.domain.Users;
import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;
import com.example.microserviceuser.dto.UserRoleDetailDto;
import com.example.microserviceuser.enumtype.RoleType;
import com.example.microserviceuser.mapper.RoleMapper;
import com.example.microserviceuser.mapper.UserMapper;
import com.example.microserviceuser.mapper.UserRoleMapper;
import com.example.microserviceuser.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, RoleMapper roleMapper, UserRoleMapper userRoleMapper, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Override
    @Transactional
    public void registerUser(RegisterDto registerDto) {
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        Users user = Users.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .build();

        //mapper로 정보 저장
        userMapper.insertUser(user);


        //권한처리(USER_AUTH)
        Roles role = roleMapper.findByRoleName(RoleType.USER_AUTH.name());
        System.out.println("Role found: " + role);

        UserRoles userRole = UserRoles.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build();

        //mapper로 정보 저장
        userRoleMapper.insertUserRole(userRole);
    }

    @Override
    public String login(LoginDto loginDto) {
        Users user = userMapper.findUserByEmail(loginDto.getEmail());
        if (user == null) {
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        // 사용자 권한 정보 조회
        List<UserRoleDetailDto> roles = userRoleMapper.findUserRoleByEmail(loginDto.getEmail());
        if (roles == null || roles.isEmpty()) {
            throw new RuntimeException("사용자에게 할당된 권한이 없습니다.");
        }

        // 권한 이름 추출
        List<String> roleNames = roles.stream()
                .map(UserRoleDetailDto::getRoleName)
                .collect(Collectors.toList());

        return jwtTokenProvider.generateToken(user.getUsername(), roleNames);
    }

    @Override
    public void logout() {

    }

    @Override
    public void tempSuspendRole(Long userId) {

    }

    @Override
    public void permSuspendRole(Long userId) {

    }

    @Override
    public void grantAdminRole(Long userId) {

    }


}
