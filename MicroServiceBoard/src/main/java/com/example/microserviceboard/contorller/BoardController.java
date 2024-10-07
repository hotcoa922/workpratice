package com.example.microserviceboard.contorller;


import com.example.microserviceboard.annotation.RSADecrypt;
import com.example.microserviceboard.dto.*;
import com.example.microserviceboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //게이트웨이 검증 방식으로 변경 -> @PreAuthorize("isAuthenticated()") 사용하지 않음

    // 게시물 생성 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @PostMapping("/post")
    @RSADecrypt(CreatePostDto.class)
    public ResponseEntity<String> createPost(
            @RequestBody CreatePostDto createPostDto,
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Roles") String rolesHeader) {
        try {
            boardService.createPost(createPostDto, email, rolesHeader);
            return ResponseEntity.ok("게시물이 성공적으로 생성되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 수정 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @PutMapping("/post/{postId}")
    @RSADecrypt(RSADecrypt.class)
    public ResponseEntity<String> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostDto updatePostDto,
            @RequestHeader String email,
            @RequestHeader String rolesHeader) {
        try {
            boardService.updatePost(postId, updatePostDto, email, rolesHeader);
            return ResponseEntity.ok("게시물이 성공적으로 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 삭제 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @DeleteMapping("/post/{postId}")
    @RSADecrypt(RSADecrypt.class)
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            boardService.deletePost(postId);
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글 생성 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @PostMapping("/post/{postId}/comment")
    @RSADecrypt(RSADecrypt.class)
    public ResponseEntity<String> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentDto createCommentDto,
            @RequestHeader String email,
            @RequestHeader String rolesHeader) {
        try {
            boardService.createComment(postId, createCommentDto, email, rolesHeader);
            return ResponseEntity.ok("댓글이 성공적으로 생성되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글 수정 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @PutMapping("/post/{postId}/comment/{commentId}")
    @RSADecrypt(RSADecrypt.class)
    public ResponseEntity<String> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentDto updateCommentDto,
            @RequestHeader String email,
            @RequestHeader String rolesHeader) {
        try {
            boardService.updateComment(postId, commentId, updateCommentDto, email, rolesHeader);
            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글 삭제 (로그인 O)
    @Secured({"USER_AUTH", "ADMIN_AUTH"})
    @DeleteMapping("/post/{postId}/comment/{commentId}")
    @RSADecrypt(RSADecrypt.class)
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId)  {
        try {
            boardService.deleteComment(commentId);
            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 전체 조회(로그인 X)
    @GetMapping("/posts")
    public ResponseEntity<List<PostsDto>> getAllPosts() {
        List<PostsDto> posts = boardService.getAllPosts();
        return ResponseEntity.ok(posts);
    }


}
