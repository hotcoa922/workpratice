package com.example.microserviceboard.service;

import com.example.microserviceboard.domain.Posts;

import java.util.List;

public interface BoardService {

    List<Posts> findAllPosts();     // 모든 게시글 조회
    Posts findPostById(Long id);    // ID를 기반으로 한 게시글 조회
    void createPost(Posts post);    // 새 게시글 생성
    void updatePost(Posts post);    // 게시글 수정
    void deletePost(Long id);       // 게시글 삭제
}
