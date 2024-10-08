package com.example.microserviceboard.service;


import com.example.microserviceboard.client.UserServiceClient;
import com.example.microserviceboard.domain.Comments;
import com.example.microserviceboard.domain.Posts;
import com.example.microserviceboard.dto.*;
import com.example.microserviceboard.mapper.CommentMapper;
import com.example.microserviceboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final UserServiceClient userServiceClient;

    private HttpServletRequest request;

    @Autowired
    public BoardServiceImpl(PostMapper postMapper, UserServiceClient userServiceClient, CommentMapper commentMapper, HttpServletRequest request) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.userServiceClient = userServiceClient;
        this.request = request;

    }

//    //현재 인증된 사용자의 정보를 가져오는 메소드
//    private UserDto getAuthenticatedUser() {
//        // SecurityContextHolder를 사용해 현재 인증된 사용자의 정보를 가져옴
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("인증되지 않은 사용자입니다.");
//        }
//
//        // FeignClient를 사용해 인증된 사용자 정보 가져오기
//        String token = (String) authentication.getCredentials();
//        UserDto userDto = userServiceClient.getAuthenticatedUser("Bearer " + token);
//
//        if (userDto == null) {
//            throw new RuntimeException("작성자가 존재하지 않습니다.");
//        }
//
//        return userDto;
//    }

    //해당 메소드도 더이상 필요 없음
    //더이상 SecurityContextHolder나 Feign 클라이언트를 사용하지 않고, 요청 헤더에서 사용자 정보를 직접 추출
//    private UserDto getAuthenticatedUser() {
//        String username = request.getHeader("X-User-Username");
//        String email = request.getHeader("X-User-Email"); // 이메일 헤더 추출
//        String rolesHeader = request.getHeader("X-User-Roles");
//
//        if (username == null || email == null || rolesHeader == null) {
//            throw new RuntimeException("인증되지 않은 사용자입니다.");
//        }
//
//        List<String> roles = Arrays.asList(rolesHeader.split(","));
//        UserDto userDto = new UserDto();
//        userDto.setUsername(username);
//        userDto.setEmail(email);
//        userDto.setRoles(roles);
//
//        return userDto;
//    }


    //@AuthenticationPrincipal 어노테이션을 사용하면 스프링 시큐리티에서 자동으로 현재 로그인한 사용자(인증된 사용자)의 세부 정보를 Principal 객체로 받을 수 있음 -> 이를 활용해서 현재 로그인한 사용자의 정보를 편리하게 접근할 수 있음
    //다만, 현재 진행 중인 마이크로서비스에서는 각 마이크로서비스가 독립적으로 동작하고 JWT 토큰을 통한 인증을 사용하기 때문에, Spring Security의 @AuthenticationPrincipal을 사용할 수 없dma
    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        //aspect에서 처리
        // 작성자 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 작성할 수 없습니다.");
//        }

        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Posts post = Posts.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .authorEmail(userDto.getEmail())
                .build();

        //작성
        postMapper.createPost(post);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, UpdatePostDto updatePostDto, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        // 작성자 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 수정할 수 없습니다.");
//        }

        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Posts post = postMapper.findPostById(postId);
        if(post == null){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        //작성자와 수정 요청자가 동일인인지 확인
        if(!post.getAuthorEmail().equals(userDto.getEmail())){
            throw new RuntimeException("게시글 작성자만 수정할 수 있습니다.");
        }

        Posts updatePost = Posts.builder()
                .id(postId)
                .title(updatePostDto.getTitle())
                .content(updatePostDto.getContent())
                .build();

        //수정
        postMapper.updatePost(updatePost);

    }

    @Override
    @Transactional
    public void deletePost(Long postId, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        // 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 삭제할 수 없습니다.");
//        }

        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Posts post = postMapper.findPostById(postId);
        if(post == null){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        //작성자와 수정 요청자가 동일인인지 확인
        if(!post.getAuthorEmail().equals(userDto.getEmail())){
            throw new RuntimeException("게시글 작성자만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        postMapper.deletePost(postId);
    }

    @Override
    @Transactional
    public void createComment(Long postId, CreateCommentDto createCommentDto, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        // 작성자 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 수정할 수 없습니다.");
//        }
        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Comments comment = Comments.builder()
                .postId(postId)
                .content(createCommentDto.getContent())
                .authorEmail(userDto.getEmail())
                .build();

        //작성
        commentMapper.createComment(comment);
    }

    @Override
    @Transactional
    public void updateComment(Long postId, Long commentId, UpdateCommentDto updateCommentDto, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        // 작성자 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 수정할 수 없습니다.");
//        }

        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Comments comment = commentMapper.findCommentById(commentId);
        if(comment == null){
            throw new RuntimeException("댓글이 존재하지 않습니다.");
        }
        if(!userDto.getEmail().equals(comment.getAuthorEmail())){
            throw new RuntimeException("댓글 작성자만 수정 가능합니다.");
        }

        Comments updateComment = Comments.builder()
                .id(commentId)
                .postId(postId)
                .content(updateCommentDto.getContent())
                .build();

        commentMapper.updateComment(updateComment);

    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String email, String rolesHeader) {

//        UserDto userDto = getAuthenticatedUser();

        // 권한 확인
//        List<String> roles = userDto.getRoles();
//        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
//            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 삭제할 수 없습니다.");
//        }


        List<String> roles = Arrays.asList(rolesHeader.split(","));
        UserDto userDto = UserDto.builder()
                .email(email)
                .roles(roles)
                .build();

        Comments comment = commentMapper.findCommentById(commentId);
        if(comment == null){
            throw new RuntimeException("댓글이 존재하지 않습니다.");
        }
        if(!userDto.getEmail().equals(comment.getAuthorEmail())){
            throw new RuntimeException("댓글 작성자만 수정 가능합니다.");
        }
        // 게시글 삭제
        commentMapper.deleteComment(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostsDto> getAllPosts() {
        List<Posts> posts = postMapper.findAllPosts();

        return posts.stream()
                .map(post -> PostsDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .authorEmail(post.getAuthorEmail())
                        .build())
                .collect(Collectors.toList());
    }



}



