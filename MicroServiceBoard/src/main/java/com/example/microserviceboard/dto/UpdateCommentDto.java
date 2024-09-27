package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateCommentDto {
    private Long id;
    private String content;
    private String authorEmail;
    private Long postId;

    @Builder
    public UpdateCommentDto(Long id, Long postId, String content, String authorEmail) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.authorEmail = authorEmail;
    }
}
