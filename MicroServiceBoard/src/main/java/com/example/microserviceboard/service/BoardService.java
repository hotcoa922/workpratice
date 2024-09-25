package com.example.microserviceboard.service;

import com.example.microserviceboard.dto.CreatePostDto;
import com.example.microserviceboard.dto.UpdatePostDto;

public interface BoardService {


    //게시글 작성
    void createPost(CreatePostDto createPostDto);

    //수정
    void updatePost(UpdatePostDto updatePostDto);

    //삭제
    void deletePost(Long postId, Long authorId);



}
