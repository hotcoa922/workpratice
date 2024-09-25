package com.example.microserviceboard.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class CreateCommentDto {
    private Long postId;
    private String content;
    private Long authorId;

    @Builder
    public CreateCommentDto(Long postId, String content, Long authorId) {
        this.postId = postId;
        this.content = content;
        this.authorId = authorId;
    }
}
