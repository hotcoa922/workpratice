package com.example.microserviceboard.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class CreateCommentDto {
    private Long postId;
    private String content;
    private String authorEmail;

    @Builder
    public CreateCommentDto(Long postId, String content, String authorEmail) {
        this.postId = postId;
        this.content = content;
        this.authorEmail = authorEmail;
    }
}
