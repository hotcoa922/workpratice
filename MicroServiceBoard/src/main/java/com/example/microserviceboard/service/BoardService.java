package com.example.microserviceboard.service;

import com.example.microserviceboard.dto.*;

import java.util.List;

public interface BoardService {


    //게시글 작성
    void createPost(CreatePostDto createPostDto, String email, String rolesHeader);

    //수정
    void updatePost(Long postId, UpdatePostDto updatePostDto, String email, String rolesHeader);

    //삭제
    void deletePost(Long postId, String email, String rolesHeader);

    //댓글 작성
    void createComment(Long postId, CreateCommentDto createCommentDto, String email, String rolesHeader);

    //수정
    void updateComment(Long postId, Long commentId, UpdateCommentDto updateCommentDto, String email, String rolesHeader);

    //삭제
    void deleteComment(Long commentId, String email, String rolesHeader);

    //전체 조회
    List<PostsDto> getAllPosts();


}
