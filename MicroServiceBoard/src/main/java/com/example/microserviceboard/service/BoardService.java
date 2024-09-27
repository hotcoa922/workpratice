package com.example.microserviceboard.service;

import com.example.microserviceboard.dto.CreateCommentDto;
import com.example.microserviceboard.dto.CreatePostDto;
import com.example.microserviceboard.dto.UpdateCommentDto;
import com.example.microserviceboard.dto.UpdatePostDto;

public interface BoardService {


    //게시글 작성
    void createPost(CreatePostDto createPostDto);

    //수정
    void updatePost(Long postId, UpdatePostDto updatePostDto);

    //삭제
    void deletePost(Long postId);
//
//    //댓글 작성
//    void createComment(CreateCommentDto createCommentDto);
//
//    //수정
//    void updateComment(UpdateCommentDto updateCommentDto);
//
//    //삭제
//    void deleteComment(Long commentId, Long authorEmail);


}
