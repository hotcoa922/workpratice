package com.example.microserviceboard.service;


import com.example.microserviceboard.client.UserServiceClient;
import com.example.microserviceboard.domain.Posts;
import com.example.microserviceboard.dto.CreatePostDto;
import com.example.microserviceboard.dto.UpdatePostDto;
import com.example.microserviceboard.dto.UserDto;
import com.example.microserviceboard.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final PostMapper postMapper;
    private final UserServiceClient userServiceClient;

    @Autowired
    public BoardServiceImpl(PostMapper postMapper, UserServiceClient userServiceClient) {
        this.postMapper = postMapper;
        this.userServiceClient = userServiceClient;
    }

    @Override
    @Transactional
    public void createPost(CreatePostDto createPostDto) {
        UserDto userDto = userServiceClient.getUserById(createPostDto.getAuthorId());
        if (userDto == null) {
            throw new RuntimeException("작성자가 존재하지 않습니다.");
        }

        // 작성자 권한 확인
        List<String> roles = userDto.getRoles();
        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 작성할 수 없습니다.");
        }

        Posts post = Posts.builder()
                .title(createPostDto.getTitle())
                .content(createPostDto.getContent())
                .authorId(createPostDto.getAuthorId())
                .build();

        //작성
        postMapper.insert(post);
    }

    @Override
    @Transactional
    public void updatePost(UpdatePostDto updatePostDto) {
        UserDto userDto = userServiceClient.getUserById(updatePostDto.getAuthorId());
        if (userDto == null) {
            throw new RuntimeException("작성자가 존재하지 않습니다.");
        }

        // 작성자 권한 확인
        List<String> roles = userDto.getRoles();
        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 수정할 수 없습니다.");
        }

        Posts post = postMapper.findById(updatePostDto.getId());
        if(post == null){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        Posts updatePost = Posts.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        //수정
        postMapper.update(updatePost);

    }

    @Override
    public void deletePost(Long postId, Long authorId) {
        UserDto userDto = userServiceClient.getUserById(authorId);
        if (userDto == null) {
            throw new RuntimeException("작성자가 존재하지 않습니다.");
        }

        // 권한 확인
        List<String> roles = userDto.getRoles();
        if (roles.contains("TEMP_SUSP_AUTH") || roles.contains("PERM_SUSP_AUTH")) {
            throw new RuntimeException("임시 정지 또는 영구 정지된 사용자는 글을 삭제할 수 없습니다.");
        }

        Posts post = postMapper.findById(postId);
        if(post == null){
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        // 게시글 삭제
        postMapper.delete(postId);
    }
}



