package com.example.microserviceuser.service;

import com.example.microserviceuser.domain.Roles;
import com.example.microserviceuser.domain.UserRoles;
import com.example.microserviceuser.domain.Users;
import com.example.microserviceuser.dto.LoginDto;
import com.example.microserviceuser.dto.RegisterDto;
import com.example.microserviceuser.dto.UserDto;
import com.example.microserviceuser.dto.UserRoleDetailDto;
import com.example.microserviceuser.enumtype.RoleType;
import com.example.microserviceuser.mapper.RoleMapper;
import com.example.microserviceuser.mapper.UserMapper;
import com.example.microserviceuser.mapper.UserRoleMapper;
import com.example.microserviceuser.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public UserDto getUserByUsername(String username) {
        // 사용자 정보 조회
        Users user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 사용자 권한 정보 조회
        List<UserRoleDetailDto> roles = userRoleMapper.findUserRolesByUserId(user.getId());
        List<String> roleNames = roles.stream()
                .map(UserRoleDetailDto::getRoleName)
                .collect(Collectors.toList());

        // UserDto 생성 후 반환
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }

    @Override
    @Transactional
    public void registerUser(RegisterDto registerDto) {

        Roles role = roleMapper.findByRoleName(RoleType.USER_AUTH.name());

        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        Users user = Users.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .build();
        userMapper.insertUser(user);

        UserRoles userRole = UserRoles.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build();
        userRoleMapper.insertUserRole(userRole);

        // JWT 토큰 발급
        List<String> roles = Collections.singletonList(role.getRoleName().name());
        String token = jwtTokenProvider.generateToken(user.getUsername(), roles);

        // JWT 토큰 콘솔 출력
        System.out.println("JWT Token: " + token);
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

    //    @Override
//    public void logout() {
//
//    }



    //인증여부 판단하고 사용자 정보 가져오는 메소드
    private UserDetails getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("인증되지 않은 사용자");
        }

        // UserDetails로 사용자 정보 가져오기
        return (UserDetails) authentication.getPrincipal();
    }

    //관리자 권한 확인
    private void checkAdminRole(Users user){
        boolean checkAdmin = userRoleMapper.existAdminRole(user.getEmail());

        if (!checkAdmin) {
            throw new RuntimeException("관리자 권한이 없습니다.");
        }
    }


    //관리자 권한 받기
    @Override
    @Transactional
    public void tryAdminAuth(String secretCode) {

        UserDetails userDetails = getAuthenticatedUser();

        String username = userDetails.getUsername();

        // 사용자 정보 가져오기 (username으로 DB에서 조회)
        Users user = userMapper.findUserByUsername(username);

        if(secretCode.equals("qwer1234!")){

            // 관리자 권한 부여
            Roles adminRole = roleMapper.findByRoleName(RoleType.ADMIN_AUTH.name());

            boolean checkAlreadyHasAdminRole = userRoleMapper.existAdminRole(user.getEmail());

            if(!checkAlreadyHasAdminRole){
                UserRoles userRole = UserRoles.builder()
                        .userId(user.getId())
                        .roleId(adminRole.getId())
                        .build();
                userRoleMapper.insertUserRole(userRole);
                System.out.println("관리자 권한 부여 완료");

            }else{
                throw new RuntimeException("이미 관리자 권한을 소유중입니다.");
            }
        }
        else{

            throw new RuntimeException("잘못된 시크릿코드 입니다. 전달된 코드:" + secretCode);
        }

    }

    //관리자 권한을 가지고 있는 유저가 일반 유저에게 임시정지
    @Override
    @Transactional
    public void tempSuspendRole(Long targetUserId) {

        UserDetails userDetails = getAuthenticatedUser();

        // 사용자 정보 가져오기 (username으로 DB에서 조회)
        String username = userDetails.getUsername();
        Users user = userMapper.findUserByUsername(username);

        //관리자인지 확인
        checkAdminRole(user);

        //임시정지 대상 조회
        Users targetUser = userMapper.findUserById(targetUserId);
        if (targetUser == null) {
            throw new RuntimeException("잘못된 대상입니다.");
        }

        //이미 임시 정지 상태인지 확인
        boolean checkTempSuspended = userRoleMapper.existTempSuspendRole(targetUser.getEmail());
        if (checkTempSuspended) {
            throw new RuntimeException("해당 사용자는 이미 임시 정지 상태입니다.");
        }

        Roles role = roleMapper.findByRoleName(RoleType.TEMP_SUSP_AUTH.name());
        UserRoles userRole = UserRoles.builder()
                .userId(targetUserId)
                .roleId(role.getId())
                .build();
        userRoleMapper.insertUserRole(userRole);


    }

    //30분마자 임시정지 해제 사간 여부 확인 및 해제처리
    @Scheduled(cron = "0 0/30 * * * ?")  // 매시간 0분과 30분에 실행
    @Transactional
    public void removeExpiredTempSuspendRole() {

        //임시 정지된 유저 목록
        List<Users> suspendedUsers = userMapper.findUsersWithTempSuspendRole();

        if (suspendedUsers.isEmpty()) {
            logger.info("임시 정지 해제할 사용자가 없습니다. (현재시간: {})", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            for (Users user : suspendedUsers) {
                LocalDateTime suspendTime = userRoleMapper.findTimeById(user.getId());

                // 24시간이 지나면 임시 정지 해제
                if (suspendTime.isBefore(LocalDateTime.now().minusHours(24))) {
                    // TEMP_SUSP_AUTH 권한을 제거
                    userRoleMapper.deleteUserRoleByUserIdAndRole(user.getId(), RoleType.TEMP_SUSP_AUTH.name());
                    logger.info("임시 정지 해제: 사용자 ID = {}", user.getId());
                }
            }
        }
    }

    //관리자 권한을 가지고 있는 유저가 일반 유저에게 영구정지
    @Override
    @Transactional
    public void permSuspendRole(Long targetUserId) {

        UserDetails userDetails = getAuthenticatedUser();

        // 사용자 정보 가져오기 (username으로 DB에서 조회)
        String username = userDetails.getUsername();
        Users user = userMapper.findUserByUsername(username);

        //관리자인지 확인
        checkAdminRole(user);

        //임시정지 대상 조회
        Users targetUser = userMapper.findUserById(targetUserId);
        if (targetUser == null) {
            throw new RuntimeException("잘못된 대상입니다.");
        }

        Roles role = roleMapper.findByRoleName(RoleType.PERM_SUSP_AUTH.name());
        UserRoles userRole = UserRoles.builder()
                .userId(targetUserId)
                .roleId(role.getId())
                .build();
        userRoleMapper.insertUserRole(userRole);

    }

}
