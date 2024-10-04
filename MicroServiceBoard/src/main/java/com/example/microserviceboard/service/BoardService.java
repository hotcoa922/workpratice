package com.example.microserviceboard.service;

import com.example.microserviceboard.dto.*;

import java.util.List;

public interface BoardService {


    //게시글 작성
    void createPost(CreatePostDto createPostDto);

    //수정
    void updatePost(Long postId, UpdatePostDto updatePostDto);

    //삭제
    void deletePost(Long postId);

    //댓글 작성
    void createComment(Long postId, CreateCommentDto createCommentDto);

    //수정
    void updateComment(Long postId, Long commentId, UpdateCommentDto updateCommentDto);

    //삭제
    void deleteComment(Long commentId);

    //전체 조회
    List<PostsDto> getAllPosts();


}
