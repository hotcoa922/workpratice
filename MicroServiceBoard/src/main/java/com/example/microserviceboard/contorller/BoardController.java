package com.example.microserviceboard.contorller;


import com.example.microserviceboard.dto.CreateCommentDto;
import com.example.microserviceboard.dto.CreatePostDto;
import com.example.microserviceboard.dto.UpdateCommentDto;
import com.example.microserviceboard.dto.UpdatePostDto;
import com.example.microserviceboard.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

//    // 게시물 전체 조회 (로그인 필요 없음)
//    @GetMapping("/posts")
//    public ResponseEntity<List<PostsDto>> getAllPosts() {
//        List<PostsDto> posts = boardService.getAllPosts();
//        return ResponseEntity.ok(posts);
//    }

//    // 개별 게시물 조회 (로그인 필요 없음)
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<PostsDto> getPostById(@PathVariable Long postId) {
//        PostsDto post = boardService.getPostById(postId);
//        return ResponseEntity.ok(post);
//    }


    // 게시물 생성 (로그인 O)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody CreatePostDto createPostDto) {
        try {
            boardService.createPost(createPostDto);
            return ResponseEntity.ok("게시물이 성공적으로 생성되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 수정 (로그인 O)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/post")
    public ResponseEntity<String> updatePost(@RequestBody UpdatePostDto updatePostDto) {
        try {
            boardService.updatePost(updatePostDto);
            return ResponseEntity.ok("게시물이 성공적으로 수정되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 게시물 삭제 (로그인 O)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/post/{postId}/{authorEmail}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @PathVariable Long authorEmail) {
        try {
            boardService.deletePost(postId, authorEmail);
            return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//
//    // 댓글 생성 (로그인 O)
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/comment")
//    public ResponseEntity<String> createComment(@RequestBody CreateCommentDto createCommentDto) {
//        try {
//            boardService.createComment(createCommentDto);
//            return ResponseEntity.ok("댓글이 성공적으로 생성되었습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // 댓글 수정 (로그인 O)
//    @PreAuthorize("isAuthenticated()")
//    @PutMapping("/comment")
//    public ResponseEntity<String> updateComment(@RequestBody UpdateCommentDto updateCommentDto) {
//        try {
//            boardService.updateComment(updateCommentDto);
//            return ResponseEntity.ok("댓글이 성공적으로 수정되었습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    // 댓글 삭제 (로그인 O)
//    @PreAuthorize("isAuthenticated()")
//    @DeleteMapping("/comment/{commentId}/{authorEmail}")
//    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @PathVariable Long authorEmail) {
//        try {
//            boardService.deleteComment(commentId, authorEmail);
//            return ResponseEntity.ok("댓글이 성공적으로 삭제되었습니다.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }


}
