package com.example.microserviceboard.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class CreateCommentDto {
    private Long postId;
    private String content;

    @Builder
    public CreateCommentDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
